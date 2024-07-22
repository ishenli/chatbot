package com.workdance.chatbot.ui.user;

import java.util.ArrayList;
import java.util.Random;

public class UserUtil {
    public static String getRandomAvatar() {
        Random random = new Random();
        ArrayList<String> list = new ArrayList<>();
        list.add("https://webimg.maibaapp.com/content/img/avatars/20160921/22/52/32359.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20183904/11/39/51999.jpeg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20161409/22/14/47401.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20170325/17/03/33267.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20160901/20/10/11682.jpg");
        list.add("https://webimg.maibaapp.com/content/img/avatars/20164508/18/45/09486.jpg");
        list.add("https://himg.bdimg.com/sys/portrait/item/public.1.cd7083db.YCCdYhAb3c_HTkJ5oKEIuw.jpg");
        return list.get(random.nextInt(list.size()));
    }
}
