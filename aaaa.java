package cn.stylefeng.guns.modular.dhc_yeji_new.controller;

import cn.stylefeng.guns.modular.dhc_allowance.service.AllowanceService;
import cn.stylefeng.guns.modular.dhc_sqt.utils.WeChatUtils;
import cn.stylefeng.guns.modular.dhc_yeji_new.model.yjhs.SummaryOfSalesPerformance;
import cn.stylefeng.guns.modular.dhc_yeji_new.service.PerformanceAccountingService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ErrorResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 沈泰
 * @version 1.0.0
 * @ClassName PerformanceAccountingController.java
 * @Description TODO
 * @createTime 2023/5/31 16:09
 */

@Slf4j
@RestController
@RequestMapping("/yj2.0/performanceAccounting")
@ApiResource(name = "业绩核算")
public class PerformanceAccountingController {

    @Autowired
    private PerformanceAccountingService performanceAccountingService;
    @Autowired
    private AllowanceService allowanceService;

    @GetResource(name = "获取销售团队业绩", path = "/getSalesTeam")
    public ResponseData detail(@RequestParam("deptCode")String dpt,@RequestParam("beginYears") String beginDate
            ,@RequestParam("endYears") String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<SummaryOfSalesPerformance> summaryOfSalesPerformanceList;
        try {
            summaryOfSalesPerformanceList = performanceAccountingService.getSummaryOfSalesPerformance(dpt,sdf.parse(beginDate)
                    ,sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            allowanceService.asyncSendWeChatMsg("业绩核算获取销售团队业绩失败" + WeChatUtils.LINE_BREAK + "原因:"+ e.getMessage());
            return new ErrorResponseData("500","参数解析失败，请检查参数");

        }
        return new SuccessResponseData(
                summaryOfSalesPerformanceList);
    }
}
