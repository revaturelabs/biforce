// ORM class for table 'BATTERY_TEST'
// WARNING: This class is AUTO-GENERATED. Modify at your own risk.
//
// Debug information:
// Generated date: Thu Feb 07 16:57:44 PST 2019
// For connector: org.apache.sqoop.manager.MySQLManager
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import com.cloudera.sqoop.lib.JdbcWritableBridge;
import com.cloudera.sqoop.lib.DelimiterSet;
import com.cloudera.sqoop.lib.FieldFormatter;
import com.cloudera.sqoop.lib.RecordParser;
import com.cloudera.sqoop.lib.BooleanParser;
import com.cloudera.sqoop.lib.BlobRef;
import com.cloudera.sqoop.lib.ClobRef;
import com.cloudera.sqoop.lib.LargeObjectLoader;
import com.cloudera.sqoop.lib.SqoopRecord;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BATTERY_TEST extends SqoopRecord  implements DBWritable, Writable {
  private final int PROTOCOL_VERSION = 3;
  public int getClassFormatVersion() { return PROTOCOL_VERSION; }
  public static interface FieldSetterCommand {    void setField(Object value);  }  protected ResultSet __cur_result_set;
  private Map<String, FieldSetterCommand> setters = new HashMap<String, FieldSetterCommand>();
  private void init0() {
    setters.put("SURROGATE_KEY", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        SURROGATE_KEY = (Integer)value;
      }
    });
    setters.put("TEST_TYPE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        TEST_TYPE = (Integer)value;
      }
    });
    setters.put("RAW_SCORE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        RAW_SCORE = (java.math.BigDecimal)value;
      }
    });
    setters.put("SCORE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        SCORE = (java.math.BigDecimal)value;
      }
    });
    setters.put("TEST_PERIOD", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        TEST_PERIOD = (Integer)value;
      }
    });
    setters.put("TEST_CATEGORY", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        TEST_CATEGORY = (Integer)value;
      }
    });
    setters.put("BUILDER_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        BUILDER_ID = (Integer)value;
      }
    });
    setters.put("GROUP_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        GROUP_ID = (Integer)value;
      }
    });
    setters.put("GROUP_TYPE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        GROUP_TYPE = (Integer)value;
      }
    });
    setters.put("BATTERY_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        BATTERY_ID = (Integer)value;
      }
    });
    setters.put("BATTERY_STATUS", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        BATTERY_STATUS = (Integer)value;
      }
    });
  }
  public BATTERY_TEST() {
    init0();
  }
  private Integer SURROGATE_KEY;
  public Integer get_SURROGATE_KEY() {
    return SURROGATE_KEY;
  }
  public void set_SURROGATE_KEY(Integer SURROGATE_KEY) {
    this.SURROGATE_KEY = SURROGATE_KEY;
  }
  public BATTERY_TEST with_SURROGATE_KEY(Integer SURROGATE_KEY) {
    this.SURROGATE_KEY = SURROGATE_KEY;
    return this;
  }
  private Integer TEST_TYPE;
  public Integer get_TEST_TYPE() {
    return TEST_TYPE;
  }
  public void set_TEST_TYPE(Integer TEST_TYPE) {
    this.TEST_TYPE = TEST_TYPE;
  }
  public BATTERY_TEST with_TEST_TYPE(Integer TEST_TYPE) {
    this.TEST_TYPE = TEST_TYPE;
    return this;
  }
  private java.math.BigDecimal RAW_SCORE;
  public java.math.BigDecimal get_RAW_SCORE() {
    return RAW_SCORE;
  }
  public void set_RAW_SCORE(java.math.BigDecimal RAW_SCORE) {
    this.RAW_SCORE = RAW_SCORE;
  }
  public BATTERY_TEST with_RAW_SCORE(java.math.BigDecimal RAW_SCORE) {
    this.RAW_SCORE = RAW_SCORE;
    return this;
  }
  private java.math.BigDecimal SCORE;
  public java.math.BigDecimal get_SCORE() {
    return SCORE;
  }
  public void set_SCORE(java.math.BigDecimal SCORE) {
    this.SCORE = SCORE;
  }
  public BATTERY_TEST with_SCORE(java.math.BigDecimal SCORE) {
    this.SCORE = SCORE;
    return this;
  }
  private Integer TEST_PERIOD;
  public Integer get_TEST_PERIOD() {
    return TEST_PERIOD;
  }
  public void set_TEST_PERIOD(Integer TEST_PERIOD) {
    this.TEST_PERIOD = TEST_PERIOD;
  }
  public BATTERY_TEST with_TEST_PERIOD(Integer TEST_PERIOD) {
    this.TEST_PERIOD = TEST_PERIOD;
    return this;
  }
  private Integer TEST_CATEGORY;
  public Integer get_TEST_CATEGORY() {
    return TEST_CATEGORY;
  }
  public void set_TEST_CATEGORY(Integer TEST_CATEGORY) {
    this.TEST_CATEGORY = TEST_CATEGORY;
  }
  public BATTERY_TEST with_TEST_CATEGORY(Integer TEST_CATEGORY) {
    this.TEST_CATEGORY = TEST_CATEGORY;
    return this;
  }
  private Integer BUILDER_ID;
  public Integer get_BUILDER_ID() {
    return BUILDER_ID;
  }
  public void set_BUILDER_ID(Integer BUILDER_ID) {
    this.BUILDER_ID = BUILDER_ID;
  }
  public BATTERY_TEST with_BUILDER_ID(Integer BUILDER_ID) {
    this.BUILDER_ID = BUILDER_ID;
    return this;
  }
  private Integer GROUP_ID;
  public Integer get_GROUP_ID() {
    return GROUP_ID;
  }
  public void set_GROUP_ID(Integer GROUP_ID) {
    this.GROUP_ID = GROUP_ID;
  }
  public BATTERY_TEST with_GROUP_ID(Integer GROUP_ID) {
    this.GROUP_ID = GROUP_ID;
    return this;
  }
  private Integer GROUP_TYPE;
  public Integer get_GROUP_TYPE() {
    return GROUP_TYPE;
  }
  public void set_GROUP_TYPE(Integer GROUP_TYPE) {
    this.GROUP_TYPE = GROUP_TYPE;
  }
  public BATTERY_TEST with_GROUP_TYPE(Integer GROUP_TYPE) {
    this.GROUP_TYPE = GROUP_TYPE;
    return this;
  }
  private Integer BATTERY_ID;
  public Integer get_BATTERY_ID() {
    return BATTERY_ID;
  }
  public void set_BATTERY_ID(Integer BATTERY_ID) {
    this.BATTERY_ID = BATTERY_ID;
  }
  public BATTERY_TEST with_BATTERY_ID(Integer BATTERY_ID) {
    this.BATTERY_ID = BATTERY_ID;
    return this;
  }
  private Integer BATTERY_STATUS;
  public Integer get_BATTERY_STATUS() {
    return BATTERY_STATUS;
  }
  public void set_BATTERY_STATUS(Integer BATTERY_STATUS) {
    this.BATTERY_STATUS = BATTERY_STATUS;
  }
  public BATTERY_TEST with_BATTERY_STATUS(Integer BATTERY_STATUS) {
    this.BATTERY_STATUS = BATTERY_STATUS;
    return this;
  }
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY_TEST)) {
      return false;
    }
    BATTERY_TEST that = (BATTERY_TEST) o;
    boolean equal = true;
    equal = equal && (this.SURROGATE_KEY == null ? that.SURROGATE_KEY == null : this.SURROGATE_KEY.equals(that.SURROGATE_KEY));
    equal = equal && (this.TEST_TYPE == null ? that.TEST_TYPE == null : this.TEST_TYPE.equals(that.TEST_TYPE));
    equal = equal && (this.RAW_SCORE == null ? that.RAW_SCORE == null : this.RAW_SCORE.equals(that.RAW_SCORE));
    equal = equal && (this.SCORE == null ? that.SCORE == null : this.SCORE.equals(that.SCORE));
    equal = equal && (this.TEST_PERIOD == null ? that.TEST_PERIOD == null : this.TEST_PERIOD.equals(that.TEST_PERIOD));
    equal = equal && (this.TEST_CATEGORY == null ? that.TEST_CATEGORY == null : this.TEST_CATEGORY.equals(that.TEST_CATEGORY));
    equal = equal && (this.BUILDER_ID == null ? that.BUILDER_ID == null : this.BUILDER_ID.equals(that.BUILDER_ID));
    equal = equal && (this.GROUP_ID == null ? that.GROUP_ID == null : this.GROUP_ID.equals(that.GROUP_ID));
    equal = equal && (this.GROUP_TYPE == null ? that.GROUP_TYPE == null : this.GROUP_TYPE.equals(that.GROUP_TYPE));
    equal = equal && (this.BATTERY_ID == null ? that.BATTERY_ID == null : this.BATTERY_ID.equals(that.BATTERY_ID));
    equal = equal && (this.BATTERY_STATUS == null ? that.BATTERY_STATUS == null : this.BATTERY_STATUS.equals(that.BATTERY_STATUS));
    return equal;
  }
  public boolean equals0(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY_TEST)) {
      return false;
    }
    BATTERY_TEST that = (BATTERY_TEST) o;
    boolean equal = true;
    equal = equal && (this.SURROGATE_KEY == null ? that.SURROGATE_KEY == null : this.SURROGATE_KEY.equals(that.SURROGATE_KEY));
    equal = equal && (this.TEST_TYPE == null ? that.TEST_TYPE == null : this.TEST_TYPE.equals(that.TEST_TYPE));
    equal = equal && (this.RAW_SCORE == null ? that.RAW_SCORE == null : this.RAW_SCORE.equals(that.RAW_SCORE));
    equal = equal && (this.SCORE == null ? that.SCORE == null : this.SCORE.equals(that.SCORE));
    equal = equal && (this.TEST_PERIOD == null ? that.TEST_PERIOD == null : this.TEST_PERIOD.equals(that.TEST_PERIOD));
    equal = equal && (this.TEST_CATEGORY == null ? that.TEST_CATEGORY == null : this.TEST_CATEGORY.equals(that.TEST_CATEGORY));
    equal = equal && (this.BUILDER_ID == null ? that.BUILDER_ID == null : this.BUILDER_ID.equals(that.BUILDER_ID));
    equal = equal && (this.GROUP_ID == null ? that.GROUP_ID == null : this.GROUP_ID.equals(that.GROUP_ID));
    equal = equal && (this.GROUP_TYPE == null ? that.GROUP_TYPE == null : this.GROUP_TYPE.equals(that.GROUP_TYPE));
    equal = equal && (this.BATTERY_ID == null ? that.BATTERY_ID == null : this.BATTERY_ID.equals(that.BATTERY_ID));
    equal = equal && (this.BATTERY_STATUS == null ? that.BATTERY_STATUS == null : this.BATTERY_STATUS.equals(that.BATTERY_STATUS));
    return equal;
  }
  public void readFields(ResultSet __dbResults) throws SQLException {
    this.__cur_result_set = __dbResults;
    this.SURROGATE_KEY = JdbcWritableBridge.readInteger(1, __dbResults);
    this.TEST_TYPE = JdbcWritableBridge.readInteger(2, __dbResults);
    this.RAW_SCORE = JdbcWritableBridge.readBigDecimal(3, __dbResults);
    this.SCORE = JdbcWritableBridge.readBigDecimal(4, __dbResults);
    this.TEST_PERIOD = JdbcWritableBridge.readInteger(5, __dbResults);
    this.TEST_CATEGORY = JdbcWritableBridge.readInteger(6, __dbResults);
    this.BUILDER_ID = JdbcWritableBridge.readInteger(7, __dbResults);
    this.GROUP_ID = JdbcWritableBridge.readInteger(8, __dbResults);
    this.GROUP_TYPE = JdbcWritableBridge.readInteger(9, __dbResults);
    this.BATTERY_ID = JdbcWritableBridge.readInteger(10, __dbResults);
    this.BATTERY_STATUS = JdbcWritableBridge.readInteger(11, __dbResults);
  }
  public void readFields0(ResultSet __dbResults) throws SQLException {
    this.SURROGATE_KEY = JdbcWritableBridge.readInteger(1, __dbResults);
    this.TEST_TYPE = JdbcWritableBridge.readInteger(2, __dbResults);
    this.RAW_SCORE = JdbcWritableBridge.readBigDecimal(3, __dbResults);
    this.SCORE = JdbcWritableBridge.readBigDecimal(4, __dbResults);
    this.TEST_PERIOD = JdbcWritableBridge.readInteger(5, __dbResults);
    this.TEST_CATEGORY = JdbcWritableBridge.readInteger(6, __dbResults);
    this.BUILDER_ID = JdbcWritableBridge.readInteger(7, __dbResults);
    this.GROUP_ID = JdbcWritableBridge.readInteger(8, __dbResults);
    this.GROUP_TYPE = JdbcWritableBridge.readInteger(9, __dbResults);
    this.BATTERY_ID = JdbcWritableBridge.readInteger(10, __dbResults);
    this.BATTERY_STATUS = JdbcWritableBridge.readInteger(11, __dbResults);
  }
  public void loadLargeObjects(LargeObjectLoader __loader)
      throws SQLException, IOException, InterruptedException {
  }
  public void loadLargeObjects0(LargeObjectLoader __loader)
      throws SQLException, IOException, InterruptedException {
  }
  public void write(PreparedStatement __dbStmt) throws SQLException {
    write(__dbStmt, 0);
  }

  public int write(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(SURROGATE_KEY, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(TEST_TYPE, 2 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(RAW_SCORE, 3 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(SCORE, 4 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeInteger(TEST_PERIOD, 5 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(TEST_CATEGORY, 6 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BUILDER_ID, 7 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(GROUP_ID, 8 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(GROUP_TYPE, 9 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BATTERY_ID, 10 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BATTERY_STATUS, 11 + __off, 4, __dbStmt);
    return 11;
  }
  public void write0(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(SURROGATE_KEY, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(TEST_TYPE, 2 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(RAW_SCORE, 3 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(SCORE, 4 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeInteger(TEST_PERIOD, 5 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(TEST_CATEGORY, 6 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BUILDER_ID, 7 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(GROUP_ID, 8 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(GROUP_TYPE, 9 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BATTERY_ID, 10 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BATTERY_STATUS, 11 + __off, 4, __dbStmt);
  }
  public void readFields(DataInput __dataIn) throws IOException {
this.readFields0(__dataIn);  }
  public void readFields0(DataInput __dataIn) throws IOException {
    if (__dataIn.readBoolean()) { 
        this.SURROGATE_KEY = null;
    } else {
    this.SURROGATE_KEY = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.TEST_TYPE = null;
    } else {
    this.TEST_TYPE = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.RAW_SCORE = null;
    } else {
    this.RAW_SCORE = com.cloudera.sqoop.lib.BigDecimalSerializer.readFields(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.SCORE = null;
    } else {
    this.SCORE = com.cloudera.sqoop.lib.BigDecimalSerializer.readFields(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.TEST_PERIOD = null;
    } else {
    this.TEST_PERIOD = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.TEST_CATEGORY = null;
    } else {
    this.TEST_CATEGORY = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.BUILDER_ID = null;
    } else {
    this.BUILDER_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.GROUP_ID = null;
    } else {
    this.GROUP_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.GROUP_TYPE = null;
    } else {
    this.GROUP_TYPE = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.BATTERY_ID = null;
    } else {
    this.BATTERY_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.BATTERY_STATUS = null;
    } else {
    this.BATTERY_STATUS = Integer.valueOf(__dataIn.readInt());
    }
  }
  public void write(DataOutput __dataOut) throws IOException {
    if (null == this.SURROGATE_KEY) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.SURROGATE_KEY);
    }
    if (null == this.TEST_TYPE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TEST_TYPE);
    }
    if (null == this.RAW_SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.RAW_SCORE, __dataOut);
    }
    if (null == this.SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.SCORE, __dataOut);
    }
    if (null == this.TEST_PERIOD) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TEST_PERIOD);
    }
    if (null == this.TEST_CATEGORY) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TEST_CATEGORY);
    }
    if (null == this.BUILDER_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BUILDER_ID);
    }
    if (null == this.GROUP_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.GROUP_ID);
    }
    if (null == this.GROUP_TYPE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.GROUP_TYPE);
    }
    if (null == this.BATTERY_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATTERY_ID);
    }
    if (null == this.BATTERY_STATUS) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATTERY_STATUS);
    }
  }
  public void write0(DataOutput __dataOut) throws IOException {
    if (null == this.SURROGATE_KEY) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.SURROGATE_KEY);
    }
    if (null == this.TEST_TYPE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TEST_TYPE);
    }
    if (null == this.RAW_SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.RAW_SCORE, __dataOut);
    }
    if (null == this.SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.SCORE, __dataOut);
    }
    if (null == this.TEST_PERIOD) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TEST_PERIOD);
    }
    if (null == this.TEST_CATEGORY) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TEST_CATEGORY);
    }
    if (null == this.BUILDER_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BUILDER_ID);
    }
    if (null == this.GROUP_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.GROUP_ID);
    }
    if (null == this.GROUP_TYPE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.GROUP_TYPE);
    }
    if (null == this.BATTERY_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATTERY_ID);
    }
    if (null == this.BATTERY_STATUS) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATTERY_STATUS);
    }
  }
  private static final DelimiterSet __outputDelimiters = new DelimiterSet((char) 44, (char) 10, (char) 0, (char) 0, false);
  public String toString() {
    return toString(__outputDelimiters, true);
  }
  public String toString(DelimiterSet delimiters) {
    return toString(delimiters, true);
  }
  public String toString(boolean useRecordDelim) {
    return toString(__outputDelimiters, useRecordDelim);
  }
  public String toString(DelimiterSet delimiters, boolean useRecordDelim) {
    StringBuilder __sb = new StringBuilder();
    char fieldDelim = delimiters.getFieldsTerminatedBy();
    __sb.append(FieldFormatter.escapeAndEnclose(SURROGATE_KEY==null?"null":"" + SURROGATE_KEY, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TEST_TYPE==null?"null":"" + TEST_TYPE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(RAW_SCORE==null?"null":RAW_SCORE.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(SCORE==null?"null":SCORE.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TEST_PERIOD==null?"null":"" + TEST_PERIOD, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TEST_CATEGORY==null?"null":"" + TEST_CATEGORY, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BUILDER_ID==null?"null":"" + BUILDER_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(GROUP_ID==null?"null":"" + GROUP_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(GROUP_TYPE==null?"null":"" + GROUP_TYPE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATTERY_ID==null?"null":"" + BATTERY_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATTERY_STATUS==null?"null":"" + BATTERY_STATUS, delimiters));
    if (useRecordDelim) {
      __sb.append(delimiters.getLinesTerminatedBy());
    }
    return __sb.toString();
  }
  public void toString0(DelimiterSet delimiters, StringBuilder __sb, char fieldDelim) {
    __sb.append(FieldFormatter.escapeAndEnclose(SURROGATE_KEY==null?"null":"" + SURROGATE_KEY, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TEST_TYPE==null?"null":"" + TEST_TYPE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(RAW_SCORE==null?"null":RAW_SCORE.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(SCORE==null?"null":SCORE.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TEST_PERIOD==null?"null":"" + TEST_PERIOD, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TEST_CATEGORY==null?"null":"" + TEST_CATEGORY, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BUILDER_ID==null?"null":"" + BUILDER_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(GROUP_ID==null?"null":"" + GROUP_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(GROUP_TYPE==null?"null":"" + GROUP_TYPE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATTERY_ID==null?"null":"" + BATTERY_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATTERY_STATUS==null?"null":"" + BATTERY_STATUS, delimiters));
  }
  private static final DelimiterSet __inputDelimiters = new DelimiterSet((char) 44, (char) 10, (char) 0, (char) 0, false);
  private RecordParser __parser;
  public void parse(Text __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(CharSequence __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(byte [] __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(char [] __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(ByteBuffer __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(CharBuffer __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  private void __loadFromFields(List<String> fields) {
    Iterator<String> __it = fields.listIterator();
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.SURROGATE_KEY = null; } else {
      this.SURROGATE_KEY = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TEST_TYPE = null; } else {
      this.TEST_TYPE = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.RAW_SCORE = null; } else {
      this.RAW_SCORE = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.SCORE = null; } else {
      this.SCORE = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TEST_PERIOD = null; } else {
      this.TEST_PERIOD = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TEST_CATEGORY = null; } else {
      this.TEST_CATEGORY = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BUILDER_ID = null; } else {
      this.BUILDER_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.GROUP_ID = null; } else {
      this.GROUP_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.GROUP_TYPE = null; } else {
      this.GROUP_TYPE = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATTERY_ID = null; } else {
      this.BATTERY_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATTERY_STATUS = null; } else {
      this.BATTERY_STATUS = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  private void __loadFromFields0(Iterator<String> __it) {
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.SURROGATE_KEY = null; } else {
      this.SURROGATE_KEY = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TEST_TYPE = null; } else {
      this.TEST_TYPE = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.RAW_SCORE = null; } else {
      this.RAW_SCORE = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.SCORE = null; } else {
      this.SCORE = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TEST_PERIOD = null; } else {
      this.TEST_PERIOD = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TEST_CATEGORY = null; } else {
      this.TEST_CATEGORY = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BUILDER_ID = null; } else {
      this.BUILDER_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.GROUP_ID = null; } else {
      this.GROUP_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.GROUP_TYPE = null; } else {
      this.GROUP_TYPE = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATTERY_ID = null; } else {
      this.BATTERY_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATTERY_STATUS = null; } else {
      this.BATTERY_STATUS = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  public Object clone() throws CloneNotSupportedException {
    BATTERY_TEST o = (BATTERY_TEST) super.clone();
    return o;
  }

  public void clone0(BATTERY_TEST o) throws CloneNotSupportedException {
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> __sqoop$field_map = new HashMap<String, Object>();
    __sqoop$field_map.put("SURROGATE_KEY", this.SURROGATE_KEY);
    __sqoop$field_map.put("TEST_TYPE", this.TEST_TYPE);
    __sqoop$field_map.put("RAW_SCORE", this.RAW_SCORE);
    __sqoop$field_map.put("SCORE", this.SCORE);
    __sqoop$field_map.put("TEST_PERIOD", this.TEST_PERIOD);
    __sqoop$field_map.put("TEST_CATEGORY", this.TEST_CATEGORY);
    __sqoop$field_map.put("BUILDER_ID", this.BUILDER_ID);
    __sqoop$field_map.put("GROUP_ID", this.GROUP_ID);
    __sqoop$field_map.put("GROUP_TYPE", this.GROUP_TYPE);
    __sqoop$field_map.put("BATTERY_ID", this.BATTERY_ID);
    __sqoop$field_map.put("BATTERY_STATUS", this.BATTERY_STATUS);
    return __sqoop$field_map;
  }

  public void getFieldMap0(Map<String, Object> __sqoop$field_map) {
    __sqoop$field_map.put("SURROGATE_KEY", this.SURROGATE_KEY);
    __sqoop$field_map.put("TEST_TYPE", this.TEST_TYPE);
    __sqoop$field_map.put("RAW_SCORE", this.RAW_SCORE);
    __sqoop$field_map.put("SCORE", this.SCORE);
    __sqoop$field_map.put("TEST_PERIOD", this.TEST_PERIOD);
    __sqoop$field_map.put("TEST_CATEGORY", this.TEST_CATEGORY);
    __sqoop$field_map.put("BUILDER_ID", this.BUILDER_ID);
    __sqoop$field_map.put("GROUP_ID", this.GROUP_ID);
    __sqoop$field_map.put("GROUP_TYPE", this.GROUP_TYPE);
    __sqoop$field_map.put("BATTERY_ID", this.BATTERY_ID);
    __sqoop$field_map.put("BATTERY_STATUS", this.BATTERY_STATUS);
  }

  public void setField(String __fieldName, Object __fieldVal) {
    if (!setters.containsKey(__fieldName)) {
      throw new RuntimeException("No such field:"+__fieldName);
    }
    setters.get(__fieldName).setField(__fieldVal);
  }

}
