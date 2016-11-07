package com.hanmz.service.protobuf;

/**
 * Created by hanmz on 2016/11/4.
 */
public class Main {
  public static void main(String[] args) {
    PersonBean pb = new PersonBean();
    pb.setHan("hanmz");
    pb.setI(2);
    byte[] arr = ProtoBufSerial.toProto(pb);
    for (byte b : arr) {
      System.out.println(b);
    }
    pb = ProtoBufSerial.fromProto(arr, PersonBean.class);
    System.out.println(pb.getHan());
    System.out.println(pb.getI());
  }
}
