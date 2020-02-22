package org.mmo.log.sl4j;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import org.mmo.engine.akka.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

/**
 * 处理sl4j日志转换，用于分享错误日志 ,akka-stream
 * TODO 功能待完善
 */
@Service
public class Sl4jLogService {
    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;



    @PostConstruct
    public void init(){
        Materializer materializer= ActorMaterializer.create(actorSystem);
        //TODO 文件路径配置
        Source<ByteString, CompletionStage<IOResult>> source= FileIO.fromPath(Paths.get("source_test.log"));
        Sink<ByteString,CompletionStage<IOResult>> sink=FileIO.toPath(Paths.get("target_test.log"));

        var flowToString= Framing.delimiter(ByteString.fromString("\r\n"),10000).map(x->x.utf8String());
        var flowToEntity= Flow.of(String.class).map(x->{
           ServerLog log=new ServerLog();
           var datas=x.split(" ");
           log.setTime(datas[0]);
           log.setThread(datas[1]);
           log.setLogString(datas[2]);
           return log;
        });
        var flowToByte=Flow.of(ServerLog.class).map(x->ByteString.fromString(x.toString()));

        RunnableGraph<CompletionStage<IOResult>> graph=source.via(flowToString).via(flowToEntity)
                .via(flowToByte).to(sink);
        var coms= graph.run(materializer);

      //  coms.thenAccept(System.out::println);
    }


}
