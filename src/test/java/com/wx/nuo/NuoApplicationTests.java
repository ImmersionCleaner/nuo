package com.wx.nuo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.wx.nuo.service.exception.MaoException;
import com.wx.nuo.service.utils.SyncUtils;

@SpringBootTest
class NuoApplicationTests {
    @Test
    public void sync01() {
        SyncUtils.printTimeAndThread("小白进入餐厅");
        SyncUtils.printTimeAndThread("小白点了番茄鸡蛋 + 一碗米饭");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("厨师炒菜");
            SyncUtils.sleepMills(100);
            SyncUtils.printTimeAndThread("厨师打饭");
            SyncUtils.sleepMills(100);
            return "番茄鸡蛋 + 米饭 做好了";
        });
        SyncUtils.printTimeAndThread("小白在打王者");
        SyncUtils.printTimeAndThread(String.format("%s ,小白开吃", task01.join()));
    }

    @org.junit.Test
    public void sync02() {
        SyncUtils.printTimeAndThread("小白进入餐厅");
        SyncUtils.printTimeAndThread("小白点了番茄鸡蛋 + 一碗米饭");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("厨师炒菜");
            SyncUtils.sleepMills(100);
            return "番茄鸡蛋";
        }).thenCompose(dish -> CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("服务员打饭");
            SyncUtils.sleepMills(100);
            return dish + "米饭";
        }));
        SyncUtils.printTimeAndThread("小白在打王者");
        SyncUtils.printTimeAndThread(String.format("%s ,小白开吃", task01.join()));
    }

    @org.junit.Test
    public void sync03() {
        SyncUtils.printTimeAndThread("小白进入餐厅");
        SyncUtils.printTimeAndThread("小白点了番茄鸡蛋 + 一碗米饭");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("厨师炒菜");
            SyncUtils.sleepMills(100);
            return "番茄鸡蛋";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("服务员蒸饭");
            SyncUtils.sleepMills(100);
            return "米饭";
        }), (dish, rise) -> {
            SyncUtils.printTimeAndThread("服务员打饭");
            SyncUtils.sleepMills(100);
            return String.format("%s, %s 好了", dish, rise);
        });
        SyncUtils.printTimeAndThread("小白在打王者");
        SyncUtils.printTimeAndThread(String.format("%s ,小白开吃", task01.join()));
    }

    @org.junit.Test
    public void sync04() {
        SyncUtils.printTimeAndThread("小白吃好了");
        SyncUtils.printTimeAndThread("小白 结账，要求开发票");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("服务员收款500");
            SyncUtils.sleepMills(100);
            SyncUtils.printTimeAndThread("服务员开发票，面额500");
            SyncUtils.sleepMills(200);
            return "500元发票";
        });
        SyncUtils.printTimeAndThread("小白接到朋友的电话，想打一局游戏");
        SyncUtils.printTimeAndThread(String.format("小白拿到%s ,准备回家", task01.join()));
    }

    @org.junit.Test
    public void sync05() {
        SyncUtils.printTimeAndThread("小白吃好了");
        SyncUtils.printTimeAndThread("小白 结账，要求开发票");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("服务员收款500");
            SyncUtils.sleepMills(100);
            CompletableFuture<String> task02 = CompletableFuture.supplyAsync(() -> {
                SyncUtils.printTimeAndThread("服务员开发票，面额500");
                SyncUtils.sleepMills(200);
                return "500元发票";
            });
            return task02.join();
        });
        SyncUtils.printTimeAndThread("小白接到朋友的电话，想打一局游戏");
        SyncUtils.printTimeAndThread(String.format("小白拿到%s ,准备回家", task01.join()));
    }

    @org.junit.Test
    public void sync06() {
        SyncUtils.printTimeAndThread("小白吃好了");
        SyncUtils.printTimeAndThread("小白 结账，要求开发票");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("服务员收款500");
            SyncUtils.sleepMills(100);
            return "500";
        }).thenApply(money -> {
            SyncUtils.printTimeAndThread(String.format("服务员开发票，面额%s元", money));
            SyncUtils.sleepMills(200);
            return String.format("%s元发票", money);
        });
        SyncUtils.printTimeAndThread("小白接到朋友的电话，想打一局游戏");
        SyncUtils.printTimeAndThread(String.format("小白拿到%s ,准备回家", task01.join()));
    }

    @org.junit.Test
    public void sync07() {
        SyncUtils.printTimeAndThread("小白走出餐厅，来到公交站");
        SyncUtils.printTimeAndThread("小白等待 700路 或者 800路 公交车到来");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("700路公交车正在赶来");
            SyncUtils.sleepMills(200);
            return "700路公交车到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("800路公交车正在赶来");
            SyncUtils.sleepMills(200);
            return "800路公交车到了";
        }), taskRes -> taskRes);
        SyncUtils.printTimeAndThread(String.format("%s ,小白回家", task01.join()));
    }

    @org.junit.Test
    public void sync08() {
        SyncUtils.printTimeAndThread("小白走出餐厅，来到公交站");
        SyncUtils.printTimeAndThread("小白等待 700路 或者 800路 公交车到来");

        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("700路公交车正在赶来");
            SyncUtils.sleepMills(200);
            if (true) {
                throw new RuntimeException("700路公交车没油了");
            }
            return "700路公交车到了";
        }).exceptionally(e -> {
            SyncUtils.printTimeAndThread(e.getMessage());
            SyncUtils.printTimeAndThread("700路公交车出故障了");
            SyncUtils.sleepMills(200);
            return "700公交车返程了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("800路公交车正在赶来");
            SyncUtils.sleepMills(200);
            return "800路公交车到了";
        }), taskRes -> {
            SyncUtils.printTimeAndThread(taskRes);
            if (taskRes.startsWith("700")) {
                throw new RuntimeException("公交车撞树上了...");
            }
            return taskRes;
        }).exceptionally(e -> {
            SyncUtils.printTimeAndThread(e.getMessage());
            SyncUtils.printTimeAndThread("小白叫了一辆出租车");
            SyncUtils.sleepMills(200);
            return "出租车 叫到了";
        });
        SyncUtils.printTimeAndThread(String.format("%s ,小白回家", task01.join()));
    }

    public void testException01() throws MaoException {
        throw new MaoException(1, "这是一个MaoException异常");
    }

    @org.junit.Test
    public void dealBusiness() {
        try {
            CompletableFuture<Object> task01 = CompletableFuture.supplyAsync(() -> {
                try {
                    checkLogin("MaoNuo", "123456");
                } catch (MaoException e) {
                    throw new CompletionException(e);
                }
                return null;
            }).whenComplete(new BiConsumer<Object, Throwable>() {
                @Override
                public void accept(Object obj, Throwable throwable) {
                    if (throwable == null) {
                        SyncUtils.printTimeAndThread("the Throwable is null");
                    } else {
                        SyncUtils.printTimeAndThread(throwable.getMessage());
                    }
                    if (obj == null) {
                        SyncUtils.printTimeAndThread("the result is null");
                    } else {
                        SyncUtils.printTimeAndThread(obj.toString());
                    }
                }
            });
            SyncUtils.printTimeAndThread("任务执行结果: " + task01.join());
        } catch (CompletionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("dealBusiness error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkLogin(String userName, String passWord) throws MaoException {
        if (userName.startsWith("M")) {
            throw new MaoException(360001, "invalid username");
        }
        if (passWord.length() < 8) {
            throw new MaoException(360002, "password is not safe enough");
        }
    }

    @org.junit.Test
    public void sync09() {
        String ipt01 = "input 01";
        CompletableFuture<String> task01 = CompletableFuture.supplyAsync(() -> {
            SyncUtils.printTimeAndThread("执行任务第一步" + ipt01);
            SyncUtils.sleepMills(200);
            try {
                testException01();
            } catch (MaoException e) {
                e.printStackTrace();
                throw new CompletionException(e);
            }
            return "xxxxxx";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            return "yyyyyy";
        }), (res1, res2) -> {
            return "";
        });
        SyncUtils.printTimeAndThread(String.format("执行结果: %s", task01.join()));
    }

    @Test
    public void sync10() {
        CompletableFuture<Object> task01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("111");
            int a = 1 / 0;
            return null;
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }
}
