package com.liming.debeziumtest;

import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.util.Clock;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    //private static Logger logger= LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        Properties propConfig = new Properties();
        try(InputStream propsInputStream = ClassLoader.getSystemResourceAsStream("config.properties")){
            propConfig.load(propsInputStream);
        }catch (IOException e){
            System.out.println(e.toString());
        }

        Configuration embeddiumConfig = io.debezium.config.Configuration.from(propConfig);
        EmbeddedEngine engine = EmbeddedEngine.create()
                .using(embeddiumConfig)
                .using(ClassLoader.getSystemClassLoader())
                .using(Clock.SYSTEM)
                .notifying(
                        Main::handleRecord
                ).using(
                    new EmbeddedEngine.ConnectorCallback() {
                        @Override
                        public void connectorStarted() {
                            //logger.info("connector started");
                            System.out.println("connector started");
                        }

                        @Override
                        public void connectorStopped() {
                            //logger.info("connector stropped");
                            System.out.println("connector stopped");
                        }

                        @Override
                        public void taskStarted() {
                            //logger.info("task started");
                            System.out.println("task started");
                        }

                        @Override
                        public void taskStopped() {
                            //logger.info("task stopped");
                            System.out.println("task stopped");
                        }
                    }
                ).using((success,message,error)->{
                    if (success) {
                        System.out.println(String.format("engine run success, %s",message));
                    } else {
                        System.out.println(String.format("engine run failed, %s, %s",message,error.toString()));
                    }
                })
                .build();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(engine);
        System.in.read();
    }

    private static void handleRecord(SourceRecord record){
        System.out.println("handle record start");
        Struct payload = (Struct)record.value();
        if(Objects.isNull(payload)){
            return;
        }
        String table = Optional.ofNullable(DebeziumRecordUtils.getRecordStructValue(payload,"source"))
                .map(s->s.getString("table")).orElse(null);

        //DML
        Envelope.Operation operation = DebeziumRecordUtils.getOperation(payload);
        if(Objects.nonNull(operation)){
            Struct key = (Struct)record.key();
            handleDML(key,payload,table,operation);
            return;
        }
        System.out.println("handle record end");
    }

    private static void handleDML(Struct key,Struct payload,String table,Envelope.Operation operation){

    }


}
