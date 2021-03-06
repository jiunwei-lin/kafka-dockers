package com.xiaojukeji.kafka.manager.service.service.gateway;

import com.xiaojukeji.kafka.manager.common.bizenum.TopicAuthorityEnum;
import com.xiaojukeji.kafka.manager.common.entity.ResultStatus;
import com.xiaojukeji.kafka.manager.common.entity.ao.gateway.TopicQuota;
import com.xiaojukeji.kafka.manager.common.entity.pojo.gateway.AuthorityDO;
import com.xiaojukeji.kafka.manager.service.config.BaseTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xuguang
 * @Date 2021/12/6
 */
public class AuthorityServiceTest extends BaseTest {

    @Autowired
    private AuthorityService authorityService;

    @DataProvider(name = "provideAuthorityDO")
    public Object[][] provideAuthorityDO() {
        AuthorityDO authorityDO = new AuthorityDO();
        authorityDO.setId(4L);
        authorityDO.setAppId("appIdModuleTest");
        authorityDO.setClusterId(1L);
        authorityDO.setTopicName("topicModuleTest");
        authorityDO.setAccess(2);
        authorityDO.setCreateTime(new Date(1638786493173L));
        authorityDO.setModifyTime(new Date(1638786493173L));
        return new Object[][] {{authorityDO}};
    }

    public TopicQuota getTopicQuota() {
        TopicQuota topicQuotaDO = new TopicQuota();
        topicQuotaDO.setAppId("testAppId");
        topicQuotaDO.setClusterId(1L);
        topicQuotaDO.setTopicName("moduleTest");
        topicQuotaDO.setProduceQuota(100000L);
        topicQuotaDO.setConsumeQuota(100000L);
        return topicQuotaDO;
    }

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "provideAuthorityDO")
    public void addAuthorityTest(AuthorityDO authorityDO) {
        // ????????????????????????
        addNewAuthority(authorityDO);
        // ??????????????????????????????
        newAccessEqualOldAccessTest(authorityDO);
        // ????????????????????????????????????
        addNewAuthorityAccessTest(authorityDO);
        // ????????????????????????
        addNewAuthority2Failure();
    }

    private void addNewAuthority(AuthorityDO authorityDO) {
        int result = authorityService.addAuthority(authorityDO);
        Assert.assertEquals(result, 1);
    }

    private void newAccessEqualOldAccessTest(AuthorityDO authorityDO) {
        int result = authorityService.addAuthority(authorityDO);
        Assert.assertEquals(result, 0);
    }

    private void addNewAuthorityAccessTest(AuthorityDO authorityDO) {
        authorityDO.setAccess(3);
        int result = authorityService.addAuthority(authorityDO);
        Assert.assertEquals(result, 1);
    }

    private void addNewAuthority2Failure() {
        int result = authorityService.addAuthority(new AuthorityDO());
        Assert.assertEquals(result, 0);
    }

    @Test(dataProvider = "provideAuthorityDO", description = "????????????????????????")
    public void deleteSpecifiedAccess(AuthorityDO authorityDO) {
        // ???????????????????????????????????????
        deleteSpecifiedAccess2AuthorityNotExist();
        // ???????????????????????????????????????
        deleteSpecifiedAccess2ParamIllegal(authorityDO);
        // ??????????????????????????????
        deleteSpecifiedAccess2Success(authorityDO);
    }

    private void deleteSpecifiedAccess2AuthorityNotExist() {
        ResultStatus result = authorityService.deleteSpecifiedAccess("xxx", 1L, "moduleTest", 2, "admin");
        Assert.assertEquals(ResultStatus.AUTHORITY_NOT_EXIST.getCode(), result.getCode());
    }

    private void deleteSpecifiedAccess2ParamIllegal(AuthorityDO authorityDO) {
        authorityService.addAuthority(authorityDO);

        ResultStatus result = authorityService.deleteSpecifiedAccess(
                authorityDO.getAppId(),
                authorityDO.getClusterId(),
                authorityDO.getTopicName(),
                3, "admin"
        );
        Assert.assertEquals(result.getCode(), ResultStatus.PARAM_ILLEGAL.getCode());
    }

    private void deleteSpecifiedAccess2Success(AuthorityDO authorityDO) {
        authorityDO.setAccess(3);
        authorityDO.setAppId("sss");
        authorityService.addAuthority(authorityDO);

        ResultStatus result = authorityService.deleteSpecifiedAccess(
                authorityDO.getAppId(),
                authorityDO.getClusterId(),
                authorityDO.getTopicName(),
                3, "admin"
        );
        Assert.assertEquals(ResultStatus.SUCCESS.getCode(), result.getCode());
    }

    @Test(dataProvider = "provideAuthorityDO", description = "????????????")
    public void getAuthorityTest(AuthorityDO authorityDO) {
        // ??????????????????
        getAuthority2SuccessTest(authorityDO);
        // ???????????????null
        getAuthority2NullTest();
    }

    private void getAuthority2SuccessTest(AuthorityDO authorityDO) {
        authorityService.addAuthority(authorityDO);

        AuthorityDO result = authorityService.getAuthority(authorityDO.getClusterId(), authorityDO.getTopicName(), authorityDO.getAppId());
        Assert.assertEquals(result.getClusterId(), authorityDO.getClusterId());
        Assert.assertEquals(result.getAppId(), authorityDO.getAppId());
        Assert.assertEquals(result.getTopicName(), authorityDO.getTopicName());
        Assert.assertEquals(result.getAccess(), authorityDO.getAccess());
    }

    private void getAuthority2NullTest() {
        AuthorityDO result = authorityService.getAuthority(10L, "moduleTest", "testAppId");
        Assert.assertNull(result);
    }

    @Test(dataProvider = "provideAuthorityDO", description = "????????????")
    public void getAuthorityByTopicTest(AuthorityDO authorityDO) {
        // ??????????????????
        getAuthorityByTopic2SuccessTest(authorityDO);
        // ???????????????null
        getAuthorityByTopic2NullTest();
    }

    private void getAuthorityByTopic2SuccessTest(AuthorityDO authorityDO) {
        authorityService.addAuthority(authorityDO);

        List<AuthorityDO> result = authorityService.getAuthorityByTopic(authorityDO.getClusterId(), authorityDO.getTopicName());
        Assert.assertNotNull(result);
        Assert.assertTrue(result.stream()
                .allMatch(authorityDO1 -> authorityDO1.getTopicName().equals(authorityDO.getTopicName()) &&
                        authorityDO1.getClusterId().equals(authorityDO.getClusterId())));
    }

    private void getAuthorityByTopic2NullTest() {
        List<AuthorityDO> result = authorityService.getAuthorityByTopic(100L, "moduleTestxxx");
        Assert.assertTrue(result.isEmpty());
    }

    @Test(dataProvider = "provideAuthorityDO", description = "????????????")
    public void getAuthorityByAppIdTest(AuthorityDO authorityDO) {
        // ??????????????????
        getAuthorityByAppId2SuccessTest(authorityDO);
        // ???????????????null
        getAuthorityByAppId2NullTest();
    }

    private void getAuthorityByAppId2SuccessTest(AuthorityDO authorityDO) {
        authorityService.addAuthority(authorityDO);

        List<AuthorityDO> result = authorityService.getAuthority(authorityDO.getAppId());
        Assert.assertNotNull(result);
        Assert.assertTrue(result.stream().
                allMatch(authorityDO1 -> authorityDO1.getAppId().equals(authorityDO.getAppId()) &&
                        !authorityDO1.getAccess().equals(TopicAuthorityEnum.DENY.getCode())));
    }

    private void getAuthorityByAppId2NullTest() {
        List<AuthorityDO> result = authorityService.getAuthority("xxx");
        Assert.assertTrue(result.isEmpty());
    }

    @Test(dataProvider = "provideAuthorityDO", description = "???????????????quota")
    public void addAuthorityAndQuotaTest(AuthorityDO authorityDO) {
        // ???????????????quota??????
        addAuthorityAndQuota2SuccessTest(authorityDO);
        // ???????????????quota??????
        addAuthorityAndQuota2FaliureTest(authorityDO);
    }

    private void addAuthorityAndQuota2SuccessTest(AuthorityDO authorityDO) {
        int result = authorityService.addAuthorityAndQuota(authorityDO, getTopicQuota());
        Assert.assertEquals(result, 1);
    }

    private void addAuthorityAndQuota2FaliureTest(AuthorityDO authorityDO) {
        authorityService.addAuthority(authorityDO);
        // ????????????
        int result2 = authorityService.addAuthorityAndQuota(authorityDO, getTopicQuota());
        Assert.assertEquals(result2, 0);
    }

    @Test(dataProvider = "provideAuthorityDO", description = "????????????")
    public void deleteAuthorityByTopicTest(AuthorityDO authorityDO) {
        // ??????????????????
        deleteAuthorityByTopic2SuccessTest(authorityDO);
        // ??????????????????
        deleteAuthorityByTopic2FailureTest();
    }

    private void deleteAuthorityByTopic2SuccessTest(AuthorityDO authorityDO) {
        authorityService.addAuthority(authorityDO);
        int result = authorityService.deleteAuthorityByTopic(authorityDO.getClusterId(), authorityDO.getTopicName());
        Assert.assertEquals(result, 1);
    }

    private void deleteAuthorityByTopic2FailureTest() {
        int result = authorityService.deleteAuthorityByTopic(100L, "moduleTest");
        Assert.assertEquals(result, 0);
    }
}
