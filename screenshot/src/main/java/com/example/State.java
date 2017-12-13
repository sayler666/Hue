/**
 * Created by sayler666 on 2015-12-20.
 * <p/>
 * Copyright 2015 MiQUiDO <http://www.miquido.com/>. All rights reserved.
 */
package com.example;

import java.util.Arrays;

/**
 * TODO Add class description...
 *
 * @author sayler666
 */
public class State {
  public double[] xy = new double[2];
  public int bri;
  public boolean on = true;

  public State(double x, double y, int bri) {
    xy[0] = x;
    xy[1] = y;
    this.bri = bri;
  }

  @Override
  public String toString() {
    return "State{" +
        "xy=" + Arrays.toString(xy) +
        ", bri=" + bri +
        ", on =" + on +
        '}';
  }
}
