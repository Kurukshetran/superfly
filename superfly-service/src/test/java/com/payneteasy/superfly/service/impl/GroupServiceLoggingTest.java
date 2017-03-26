package com.payneteasy.superfly.service.impl;


import com.payneteasy.superfly.dao.GroupDao;
import com.payneteasy.superfly.model.ui.group.UICloneGroupRequest;
import com.payneteasy.superfly.model.ui.group.UIGroup;
import com.payneteasy.superfly.service.GroupService;
import com.payneteasy.superfly.service.NotificationService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Collections;

import static org.easymock.EasyMock.*;

public class GroupServiceLoggingTest extends AbstractServiceLoggingTest {

    private GroupService groupService;
    private GroupDao groupDao;

    @Before
    public void setUp() {
        GroupServiceImpl service = new GroupServiceImpl();
        groupDao = EasyMock.createStrictMock(GroupDao.class);
        service.setGroupDao(groupDao);
        service.setNotificationService(TrivialProxyFactory.createProxy(NotificationService.class));
        service.setLoggerSink(loggerSink);
        groupService = service;
    }

    @Test
    public void testCreateGroup() throws Exception {
        groupDao.createGroup(anyObject(UIGroup.class));
        EasyMock.expectLastCall().andReturn(okResult());
        loggerSink.info(anyObject(Logger.class), eq("CREATE_GROUP"), eq(true), eq("test-group"));
        EasyMock.replay(loggerSink, groupDao);

        UIGroup group = new UIGroup();
        group.setName("test-group");
        groupService.createGroup(group);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testCreateGroupFail() throws Exception {
        groupDao.createGroup(anyObject(UIGroup.class));
        EasyMock.expectLastCall().andReturn(failureResult());
        loggerSink.info(anyObject(Logger.class), eq("CREATE_GROUP"), eq(false), eq("test-group"));
        EasyMock.replay(loggerSink, groupDao);

        UIGroup group = new UIGroup();
        group.setName("test-group");
        groupService.createGroup(group);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testUpdateGroup() throws Exception {
        groupDao.updateGroup(anyLong(), anyObject(String.class));
        EasyMock.expectLastCall().andReturn(okResult());
        loggerSink.info(anyObject(Logger.class), eq("UPDATE_GROUP"), eq(true), eq("test-group"));
        EasyMock.replay(loggerSink, groupDao);

        UIGroup group = new UIGroup();
        group.setName("test-group");
        groupService.updateGroup(group);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testUpdateGroupFail() throws Exception {
        groupDao.updateGroup(anyLong(), anyObject(String.class));
        EasyMock.expectLastCall().andReturn(failureResult());
        loggerSink.info(anyObject(Logger.class), eq("UPDATE_GROUP"), eq(false), eq("test-group"));
        EasyMock.replay(loggerSink, groupDao);

        UIGroup group = new UIGroup();
        group.setName("test-group");
        groupService.updateGroup(group);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testDeleteGroup() throws Exception {
        groupDao.deleteGroup(anyLong());
        EasyMock.expectLastCall().andReturn(okResult());
        loggerSink.info(anyObject(Logger.class), eq("DELETE_GROUP"), eq(true), eq("1"));
        EasyMock.replay(loggerSink, groupDao);

        groupService.deleteGroup(1L);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testDeleteGroupFail() throws Exception {
        groupDao.deleteGroup(anyLong());
        EasyMock.expectLastCall().andReturn(failureResult());
        loggerSink.info(anyObject(Logger.class), eq("DELETE_GROUP"), eq(false), eq("1"));
        EasyMock.replay(loggerSink, groupDao);

        groupService.deleteGroup(1L);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testCloneGroup() throws Exception {
        groupDao.cloneGroup(anyObject(UICloneGroupRequest.class));
        EasyMock.expectLastCall().andReturn(okResult());
        loggerSink.info(anyObject(Logger.class), eq("CLONE_GROUP"), eq(true), eq("1->new-group"));
        EasyMock.replay(loggerSink, groupDao);

        UICloneGroupRequest request = new UICloneGroupRequest();
        request.setSourceGroupId(1L);
        request.setNewGroupName("new-group");
        groupService.cloneGroup(request);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testCloneGroupFail() throws Exception {
        groupDao.cloneGroup(anyObject(UICloneGroupRequest.class));
        EasyMock.expectLastCall().andReturn(failureResult());
        loggerSink.info(anyObject(Logger.class), eq("CLONE_GROUP"), eq(false), eq("1->new-group"));
        EasyMock.replay(loggerSink, groupDao);

        UICloneGroupRequest request = new UICloneGroupRequest();
        request.setSourceGroupId(1L);
        request.setNewGroupName("new-group");
        groupService.cloneGroup(request);

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testChangeGroupActions() throws Exception {
        groupDao.changeGroupActions(anyLong(), anyObject(String.class), anyObject(String.class));
        EasyMock.expectLastCall().andReturn(okResult());
        loggerSink.info(anyObject(Logger.class), eq("CHANGE_GROUP_ACTIONS"), eq(true), eq("1"));
        EasyMock.replay(loggerSink, groupDao);

        groupService.changeGroupActions(1L, Collections.<Long>emptyList(), Collections.<Long>emptyList());

        EasyMock.verify(loggerSink);
    }

    @Test
    public void testChangeGroupActionsFail() throws Exception {
        groupDao.changeGroupActions(anyLong(), anyObject(String.class), anyObject(String.class));
        EasyMock.expectLastCall().andReturn(failureResult());
        loggerSink.info(anyObject(Logger.class), eq("CHANGE_GROUP_ACTIONS"), eq(false), eq("1"));
        EasyMock.replay(loggerSink, groupDao);

        groupService.changeGroupActions(1L, Collections.<Long>emptyList(), Collections.<Long>emptyList());

        EasyMock.verify(loggerSink);
    }
}
