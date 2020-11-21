package ru.bez_createha.queue_bot.services.impl;

import ru.bez_createha.queue_bot.dao.GroupRepository;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

   private final GroupRepository groupRepository;

   @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    @Override
    public List<Group> findAllByAdmin(User admin) {
        return groupRepository.findAllByAdmin(admin);
    }

    @Override
    public void saveGroup(Group group){
        groupRepository.save(group);
    }

    @Override
    public Group findByChatId(Long chatId) {
        return groupRepository.findByChatId(chatId);
    }


}
