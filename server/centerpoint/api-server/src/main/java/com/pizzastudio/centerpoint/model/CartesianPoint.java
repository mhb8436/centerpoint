package com.pizzastudio.centerpoint.model;

public class CartesianPoint {
   private double x;
   private double y;
   private double z;

   public CartesianPoint(double lat, double lng){
//      X1 = cos(lat1) * cos(lon1)
//      Y1 = cos(lat1) * sin(lon1)
//      Z1 = sin(lat1)
      this.x = Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lng));
      this.y = Math.cos(Math.toRadians(lat)) * Math.sin(Math.toRadians(lng));
      this.z = Math.sin(Math.toRadians(lat));
   }

   public double getX() {
      return x;
   }

   public double getY() {
      return y;
   }

   public double getZ() {
      return z;
   }
}
