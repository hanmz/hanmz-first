package com.hanmz.service.protobuf;

import org.apache.ibatis.annotations.Param;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by hanmz on 2016/11/4.
 */
public class SerialTest {
  public static void main(String[] args) {
    try {
      Test tt = new Test();
      tt.tran = 100;
      tt.a = 100;
      //初始时staticVar为5
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("result.obj"));
      out.writeObject(tt);
      out.close();

      //序列化后修改为10
      Test.staticVar = 10;

      ObjectInputStream oin = new ObjectInputStream(new FileInputStream("result.obj"));
      Test t = (Test) oin.readObject();
      oin.close();

      //再读取，通过t.staticVar打印新的值
      System.out.println(t.staticVar);
      System.out.println(t.tran);
      System.out.println(tt.tran);
      System.out.println(t.a);
      System.out.println(tt.a);
      System.out.println(Test.staticVar);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


class Test extends Parent implements Serializable {
  private static final long serialVersionUID = 1L;

  public static int staticVar = 5;

  public transient int tran;
}


class Parent implements Serializable {
  int a;
}
