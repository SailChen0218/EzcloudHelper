//package cn.zjcdjk.ezcloud.test;
//
//import java.util.List;
//
//@ApplicationService(path = "/qyxx")
//public class QyxxAppServiceImpl implements QyxxAppService {
//
//    @Autowired
//    ICommandGateway commandGateway;
//
//    @CommandMapping("/findQyjbxxByqyid")
//    @Override
//    public Result<QyjbxxVo> findQyjbxxByqyid(QyjbxxByIdQuery query) {
//        return commandGateway.send(query);
//    }
//
//    @CommandMapping("/findSbpcListById")
//    @Override
//    public Result<List<SbpcVo>> findQyxxSbpcListById(QyxxSbpcListQuery query) {
//        return commandGateway.send(query);
//    }
//
//}
