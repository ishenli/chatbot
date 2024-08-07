package com.workdance.core.error;

/**
 * 错误类型常量池
 *
 * <p>对应于标准错误码的第5位
 *      <table border="1">
 *          <tr>
 *           <td><b>位置</b></td><td>1</td><td>2</td><td>3</td><td>4</td><td bgcolor="green">5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td><td>12</td>
 *          </tr>
 *          <tr>
 *           <td><b>示例</b></td><td>A</td><td>E</td><td>0</td><td>1</td><td>0</td><td>1</td><td>0</td><td>1</td><td>1</td><td>0</td><td>2</td><td>7</td>
 *          </tr>
 *          <tr>
 *           <td><b>说明</b></td><td colspan=2>固定<br>标识</td><td>规<br>范<br>版<br>本</td><td>错<br>误<br>级<br>别</td><td>错<br>误<br>类<br>型</td><td colspan=4>错误场景</td><td colspan=3>错误编<br>码</td>
 *          </tr>
 *          </table>
 *
 * @author jiapeng.li
 * @author anorld.zhangm
 * @version $Id: ErrorLevels.java, v 0.1 2012-5-22 下午12:25:50 jiapeng.li Exp $
 */
public interface ErrorTypes {

  /** 系统错误 */
  public static final String SYSTEM      = "0";

  /** 业务错误 */
  public static final String BIZ         = "1";

  /** 第三方错误 */
  public static final String THIRD_PARTY = "2";
}
