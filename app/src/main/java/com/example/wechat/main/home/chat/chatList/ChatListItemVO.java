package com.example.wechat.main.home.chat.chatList;

import java.util.ArrayList;
import java.util.Random;

public class ChatListItemVO {
    public String id;
    public String name;
    public String message;
    public String avatar;
    public String time;
    public int messageCount;

    public ChatListItemVO(String name, String message, String avatar) {
        this.id = "haomo";
        this.name = name;
        this.message = message;
        this.avatar = avatar;
        this.time = "6月7日";
        Random random = new Random();
        this.messageCount = random.nextInt(16) + 5;
    }


    public static  ArrayList<ChatListItemVO> mock() {
        int arraySize = 30; // 假设我们要生成一个包含10个ChatListItemVO对象的数组
        ArrayList<ChatListItemVO> persons = new ArrayList<>(arraySize);
        Random random = new Random();
        ArrayList<String> list = new ArrayList<>();
        list.add("https://webimg.maibaapp.com/content/img/avatars/20160921/22/52/32359.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20183904/11/39/51999.jpeg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20161409/22/14/47401.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20170325/17/03/33267.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20160901/20/10/11682.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20164508/18/45/09486.jpg");
        list.add("https://himg.bdimg.com/sys/portrait/item/public.1.cd7083db.YCCdYhAb3c_HTkJ5oKEIuw.jpg");
        for (int i = 0; i < arraySize; i++) {
            // 随机生成5到20之间的整数作为名字的长度
            int nameLength = random.nextInt(30) + 5; // nextInt(16)生成0-15，加上5变为5-20
            StringBuilder nameBuilder = new StringBuilder();
            for (int j = 0; j < nameLength; j++) {
                // 生成一个随机的中文字符索引并转换为对应的中文字符
                char c = (char) (random.nextInt(94) + 0x4e00); // 0x4e00到0x9fff之间有20902个字符，这里取前94*20个简化处理
                nameBuilder.append(c);
            }

            int nameLength2 = random.nextInt(5) + 2; // nextInt(16)生成0-15，加上5变为5-20
            StringBuilder nameBuilder2 = new StringBuilder();
            for (int j = 0; j < nameLength2; j++) {
                // 生成一个随机的中文字符索引并转换为对应的中文字符
                char c = (char) (random.nextInt(94) + 0x4e00); // 0x4e00到0x9fff之间有20902个字符，这里取前94*20个简化处理
                nameBuilder2.append(c);
            }
            String name = nameBuilder2.toString() + i;
            String message = nameBuilder.toString();
            String avatar = list.get(nameLength2);
            ChatListItemVO person = new ChatListItemVO(name, message, avatar);
            persons.add(person);
        }

        return persons;
    }
}
