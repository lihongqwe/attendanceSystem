package com.attendance.system.impl;


import com.attendance.domain.AttendanceGroup;
import com.attendance.domain.studentAttendanceInfo;
import com.attendance.domain.studentUserInfo;
import com.attendance.mapper.studentAttendanceInfoMapper;
import com.attendance.mapper.studentUserInfoMapper;
import com.attendance.system.ScheduledTasks;
import com.attendance.system.TokenService;
import com.attendance.utils.StringUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.attendance.common.UrlConstant.*;


/**
 * 计划任务
 *
 * @author lihong
 * @date 2022/09/03
 */
@Slf4j
@Service
@EnableScheduling
public class ScheduledTask implements ScheduledTasks {


    @Autowired
    private TokenService tokenService;

    @Autowired
    private studentAttendanceInfoMapper  studentAttendanceInfoMapper;

    @Autowired
    private studentUserInfoMapper UserInfoMapper;


    /**
     * 从钉钉接口获取考勤信息
     */
    @Scheduled(cron = "0 0 1 * * ?") //0 0 1 * * ? 0/10 * * * * ?
    public void GetAttendanceInfoFromDingTalk(){
        String access_token = tokenService.AccessToken();
        DingTalkClient  clientDingTalkClient = new DefaultDingTalkClient(GET_PUNCH_RESULTS);
        OapiAttendanceListRecordRequest request = new OapiAttendanceListRecordRequest ();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date startdate = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(startdate);
//        now.add(Calendar.HOUR, -49);
        now.add(Calendar.HOUR, -19);
        // 查询考勤打卡记录的起始工作日
        request.setCheckDateFrom(dateFormat.format(now.getTime()));
        // 查询考勤打卡记录的结束工作日
        Date Endtdate = new Date();
        Calendar sss = Calendar.getInstance();
        sss.setTime(Endtdate);
//        sss.add(Calendar.HOUR, -14);//HOUR
        sss.add(Calendar.HOUR, -2);//HOUR
        request.setCheckDateTo(dateFormat.format(sss.getTime()));
        request.setIsI18n(false);
        long offset = 0L;
        int end = 50;
        int star = 0;
        boolean HasMore = true;
        List<String> AllUserIdList =getStudentIdList();
        List<String> new_data_list;
        int index = AllUserIdList.size()/50;
        int a=1;
        while (HasMore) {
            // 员工在企业内的userid列表，最多不能超过50个。
            new_data_list = AllUserIdList.subList(star, end);
            if(a<=index){
                star = end;
                end +=50;
                request.setUserIds(new_data_list);
                a++;
            }
            else {
                request.setUserIds(AllUserIdList.subList(50*a,AllUserIdList.size()));
                HasMore = false;
            }
            while (true) {
                OapiAttendanceListRecordResponse response = new OapiAttendanceListRecordResponse ();
                try {
                    response = clientDingTalkClient.execute(request, access_token);
                } catch (ApiException e) {
                    // TODO Auto-generated catch block
                }
                if(StringUtils.isNull(response)){
                    log.error("获取打卡信息失败");
                    break;
                }
                //获取所有人打卡信息
                List<studentAttendanceInfo>AllAttendanceInfoList=new ArrayList<>();
                int Length = response.getRecordresult().size();
                for (int i = 0; i < Length; i++){
                    studentAttendanceInfo  studentAttendanceInfo=new studentAttendanceInfo();
                    OapiAttendanceListRecordResponse .Recordresult result = response.getRecordresult().get(i);
                    studentAttendanceInfo.setUserId(result.getUserId());
                    studentAttendanceInfo.setUserCheckTime(result.getUserCheckTime());
                    studentAttendanceInfo.setCheckType(result.getCheckType());
                    studentAttendanceInfo.setGroupid(result.getGroupId());
                    studentAttendanceInfo.setTimeResult(result.getTimeResult());
                    AllAttendanceInfoList.add(studentAttendanceInfo);
                }
                List<studentAttendanceInfo>StudentAttendanceInfoList=new ArrayList<>();
                //获取学生打卡信息
                for(studentAttendanceInfo studentAttendanceInfo:AllAttendanceInfoList){
                    Long employeeAttendanceClock = 915430240L;
                    Long teacherAttendanceClock = 915430240L;
                    if(!Objects.equals(studentAttendanceInfo.getGroupid(), teacherAttendanceClock)
                            && !Objects.equals(studentAttendanceInfo.getGroupid(), employeeAttendanceClock)){
                        StudentAttendanceInfoList.add(studentAttendanceInfo);
                    }
                }
                if(StudentAttendanceInfoList.size()!=0){
                    studentAttendanceInfoMapper.insert(StudentAttendanceInfoList);
                }
                    log.info("数据获取完毕");
                    break;
            }
            offset += 50L;
            index --;
        }
        log.info("考勤数据持久化成功");
    }

    /**
     * 获取用户名
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void getUserName(){
        String access_token = tokenService.AccessToken();
        DingTalkClient clientDingTalkClient = new DefaultDingTalkClient(GET_USER_INFORMATION);
        OapiV2UserListRequest  request = new OapiV2UserListRequest();
        List<OapiV2UserListResponse.ListUserResponse> list= new ArrayList<>();
        for(Long StudentDeptId:GetStudentDeptId()){
            request.setDeptId(StudentDeptId);
            request.setCursor(0L);
            request.setSize(100L);
            request.setOrderField("modify_desc");
            request.setContainAccessLimit(false);
            request.setLanguage("zh_CN");
            OapiV2UserListResponse response = null;
            try {
                response = clientDingTalkClient.execute(request, access_token);
            }catch (Exception e){
                log.error("获取用户信息失败");
            }
            assert response != null;
            list.addAll(response.getResult().getList());
        }
        List<studentUserInfo> studentUserInfoList=new ArrayList<>();
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        for(OapiV2UserListResponse.ListUserResponse listUserResponse:list){
            studentUserInfo userInfo=new studentUserInfo();
            userInfo.setUserId(listUserResponse.getUserid());
            userInfo.setUsername(listUserResponse.getName());
            userInfo.setPhonenumber(listUserResponse.getMobile());
            //学生默认密码为123456
            userInfo.setPassword(bCryptPasswordEncoder.encode("123456"));
            userInfo.setRoles("common");
            studentUserInfoList.add(userInfo);
        }
        if(UserInfoMapper.selectAll().size()!=studentUserInfoList.size()&&UserInfoMapper.delete()==0){
            UserInfoMapper.insertList(studentUserInfoList);
            log.info("用户信息更新成功");
        }
    }


    /**
     * 得到学生部门id
     * 1.694230362 实习学生
     * 2.694413354 学生
     * ---------------------
     * 比特元科技,684962050
     * cto学院学生,694400432
     * 大四学生,703331133
     * @return {@link List}<{@link Long}>
     */
    public List<Long> GetStudentDeptId(){
        String access_token = tokenService.AccessToken();
        DingTalkClient client = new DefaultDingTalkClient(Get_DeptId);
        OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
        List<Long> StudentDeptId=new ArrayList<>();
        Long dept=1L;
        request.setDeptId(dept);
        request.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse response = null;
        try{
            response= client.execute(request, access_token);
        }catch (Exception e){
            log.error("获取部门ID失败");
        }
        assert response != null;
        for(OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse:response.getResult()){
            if(deptBaseResponse.getDeptId()!=684962050){
                List<Long> Dept= WhetherSubDepartments(deptBaseResponse.getDeptId());
                StudentDeptId.addAll(Dept);
                if(Dept.size()==0){
                    StudentDeptId.add(deptBaseResponse.getDeptId());
                }
            }
        }
        StudentDeptId.add(1L);
        System.out.println(StudentDeptId);
        return StudentDeptId;
    }

    /**
     * 子部门是否
     *
     * @param deptId 部门id
     * @return boolean
     */
    public List<Long> WhetherSubDepartments(Long deptId){
        String access_token = tokenService.AccessToken();
        DingTalkClient clients = new DefaultDingTalkClient(Get);
        OapiV2DepartmentListsubidRequest  req = new OapiV2DepartmentListsubidRequest ();
        req.setDeptId(deptId);
        OapiV2DepartmentListsubidResponse response;
        try {
            response = clients.execute(req, access_token);
        }catch (Exception e){
            throw new RuntimeException("获取部门详情失败");
        }
        return response.getResult().getDeptIdList();
    }



    /**
     * 获取考勤组信息
     *
     * @return {@link List}<{@link AttendanceGroup}>
     */
    @Override
    public List<AttendanceGroup> GetAttendanceGroup(){
        String access_token = tokenService.AccessToken();
        DingTalkClient client = new DefaultDingTalkClient(GET_Attendance_Group);
        OapiAttendanceGroupMinimalismListRequest req = new OapiAttendanceGroupMinimalismListRequest();
        req.setOpUserId("manager4220");
        req.setCursor(0L);
        OapiAttendanceGroupMinimalismListResponse response = null;
        try{
            response = client.execute(req, access_token);
        }catch (Exception e){
            log.error("获取考勤组信息失败");
        }
        assert response != null;
        //所有考勤组的信息
        List<OapiAttendanceGroupMinimalismListResponse.TopMinimalismGroupVo> topMinimalismGroupVos = response.getResult().getResult();
        List<AttendanceGroup> attendanceGroupList=new ArrayList<>();
        for(OapiAttendanceGroupMinimalismListResponse.TopMinimalismGroupVo AttendanceGroup:topMinimalismGroupVos){
            AttendanceGroup attendanceGroup=new AttendanceGroup();
            attendanceGroup.setName(AttendanceGroup.getName());
            attendanceGroup.setId(AttendanceGroup.getId());
            attendanceGroupList.add(attendanceGroup);
        }
        return attendanceGroupList;
    }


    /**
     * 获取学生id列表
     * 1.829660271,underdog学生考勤打卡
     * 2.907645094,电子信息产业学院教师考勤打卡
     * 3.915430240,比特元员工考勤打卡
     * 4.967575169,暑假留校学生
     * @return {@link List}<{@link String}>
     */
    public List<String> getStudentIdList() {
        String access_token = tokenService.AccessToken();
        DingTalkClient client = new DefaultDingTalkClient(GEt_USER_lISTID);
        OapiAttendanceGroupMemberusersListRequest request = new OapiAttendanceGroupMemberusersListRequest();
        request.setOpUserId("user123");
        OapiAttendanceGroupMemberusersListResponse response = null;
        List<AttendanceGroup> attendanceGroups = GetAttendanceGroup();
        //储存所有用户id
        List<String> StudentIdList=new ArrayList<>();
        for (AttendanceGroup attendanceGroup : attendanceGroups){
            //获取考勤组Id
                request.setGroupId(attendanceGroup.getId());
                try {
                    response = client.execute(request, access_token);
                } catch (ApiException e) {
                    log.error("获取学生userId异常");
                }
                assert response != null;
                StudentIdList.addAll(response.getResult().getResult());
        }
        return StudentIdList;
    }

}

