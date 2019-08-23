package com.cinkle.com.cinkle.jvmT;

import static java.lang.Thread.sleep;

public class ObjectAlive {
    static ObjectAlive obj =null;

    public void isAlive(){
        System.out.println("yes,i an still alive");
    }

    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        System.out.println("finalize method executed");
        ObjectAlive.obj = this;
    }

    public static void main(String[] args) throws Throwable{
        obj = new ObjectAlive();

        //对象第一次成功拯救自己
        obj = null;
        System.gc();
        //因为Finalizer方法优先级很低，暂停0.5秒，以等待它
        sleep(500);
        if(obj !=null){
            obj.isAlive();
        }
        else{
            System.out.println("no, i am dead");
        }


        //下面这段代码与上面的完全相同，但是这次自救却失败了
        obj = null;
        System.gc();
        //因为Finalizer方法优先级很低，暂停0.5秒，以等待它
        sleep(500);
        if(obj !=null){
            obj.isAlive();
        }
        else{
            System.out.println("no, i am dead");
        }
    }
}
