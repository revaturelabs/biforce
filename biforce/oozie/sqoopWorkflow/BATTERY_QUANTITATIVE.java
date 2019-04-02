// ORM class for table 'BATTERY_QUANTITATIVE'
// WARNING: This class is AUTO-GENERATED. Modify at your own risk.
//
// Debug information:
// Generated date: Thu Feb 07 16:45:36 PST 2019
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

public class BATTERY_QUANTITATIVE extends SqoopRecord  implements DBWritable, Writable {
  private final int PROTOCOL_VERSION = 3;
  public int getClassFormatVersion() { return PROTOCOL_VERSION; }
  public static interface FieldSetterCommand {    void setField(Object value);  }  protected ResultSet __cur_result_set;
  private Map<String, FieldSetterCommand> setters = new HashMap<String, FieldSetterCommand>();
  private void init0() {
    setters.put("GRADE_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        GRADE_ID = (Integer)value;
      }
    });
    setters.put("SCORE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        SCORE = (java.math.BigDecimal)value;
      }
    });
    setters.put("ASSESSMENT_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        ASSESSMENT_ID = (Integer)value;
      }
    });
    setters.put("TRAINEE_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        TRAINEE_ID = (Integer)value;
      }
    });
  }
  public BATTERY_QUANTITATIVE() {
    init0();
  }
  private Integer GRADE_ID;
  public Integer get_GRADE_ID() {
    return GRADE_ID;
  }
  public void set_GRADE_ID(Integer GRADE_ID) {
    this.GRADE_ID = GRADE_ID;
  }
  public BATTERY_QUANTITATIVE with_GRADE_ID(Integer GRADE_ID) {
    this.GRADE_ID = GRADE_ID;
    return this;
  }
  private java.math.BigDecimal SCORE;
  public java.math.BigDecimal get_SCORE() {
    return SCORE;
  }
  public void set_SCORE(java.math.BigDecimal SCORE) {
    this.SCORE = SCORE;
  }
  public BATTERY_QUANTITATIVE with_SCORE(java.math.BigDecimal SCORE) {
    this.SCORE = SCORE;
    return this;
  }
  private Integer ASSESSMENT_ID;
  public Integer get_ASSESSMENT_ID() {
    return ASSESSMENT_ID;
  }
  public void set_ASSESSMENT_ID(Integer ASSESSMENT_ID) {
    this.ASSESSMENT_ID = ASSESSMENT_ID;
  }
  public BATTERY_QUANTITATIVE with_ASSESSMENT_ID(Integer ASSESSMENT_ID) {
    this.ASSESSMENT_ID = ASSESSMENT_ID;
    return this;
  }
  private Integer TRAINEE_ID;
  public Integer get_TRAINEE_ID() {
    return TRAINEE_ID;
  }
  public void set_TRAINEE_ID(Integer TRAINEE_ID) {
    this.TRAINEE_ID = TRAINEE_ID;
  }
  public BATTERY_QUANTITATIVE with_TRAINEE_ID(Integer TRAINEE_ID) {
    this.TRAINEE_ID = TRAINEE_ID;
    return this;
  }
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY_QUANTITATIVE)) {
      return false;
    }
    BATTERY_QUANTITATIVE that = (BATTERY_QUANTITATIVE) o;
    boolean equal = true;
    equal = equal && (this.GRADE_ID == null ? that.GRADE_ID == null : this.GRADE_ID.equals(that.GRADE_ID));
    equal = equal && (this.SCORE == null ? that.SCORE == null : this.SCORE.equals(that.SCORE));
    equal = equal && (this.ASSESSMENT_ID == null ? that.ASSESSMENT_ID == null : this.ASSESSMENT_ID.equals(that.ASSESSMENT_ID));
    equal = equal && (this.TRAINEE_ID == null ? that.TRAINEE_ID == null : this.TRAINEE_ID.equals(that.TRAINEE_ID));
    return equal;
  }
  public boolean equals0(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY_QUANTITATIVE)) {
      return false;
    }
    BATTERY_QUANTITATIVE that = (BATTERY_QUANTITATIVE) o;
    boolean equal = true;
    equal = equal && (this.GRADE_ID == null ? that.GRADE_ID == null : this.GRADE_ID.equals(that.GRADE_ID));
    equal = equal && (this.SCORE == null ? that.SCORE == null : this.SCORE.equals(that.SCORE));
    equal = equal && (this.ASSESSMENT_ID == null ? that.ASSESSMENT_ID == null : this.ASSESSMENT_ID.equals(that.ASSESSMENT_ID));
    equal = equal && (this.TRAINEE_ID == null ? that.TRAINEE_ID == null : this.TRAINEE_ID.equals(that.TRAINEE_ID));
    return equal;
  }
  public void readFields(ResultSet __dbResults) throws SQLException {
    this.__cur_result_set = __dbResults;
    this.GRADE_ID = JdbcWritableBridge.readInteger(1, __dbResults);
    this.SCORE = JdbcWritableBridge.readBigDecimal(2, __dbResults);
    this.ASSESSMENT_ID = JdbcWritableBridge.readInteger(3, __dbResults);
    this.TRAINEE_ID = JdbcWritableBridge.readInteger(4, __dbResults);
  }
  public void readFields0(ResultSet __dbResults) throws SQLException {
    this.GRADE_ID = JdbcWritableBridge.readInteger(1, __dbResults);
    this.SCORE = JdbcWritableBridge.readBigDecimal(2, __dbResults);
    this.ASSESSMENT_ID = JdbcWritableBridge.readInteger(3, __dbResults);
    this.TRAINEE_ID = JdbcWritableBridge.readInteger(4, __dbResults);
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
    JdbcWritableBridge.writeInteger(GRADE_ID, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(SCORE, 2 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeInteger(ASSESSMENT_ID, 3 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(TRAINEE_ID, 4 + __off, 4, __dbStmt);
    return 4;
  }
  public void write0(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(GRADE_ID, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(SCORE, 2 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeInteger(ASSESSMENT_ID, 3 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(TRAINEE_ID, 4 + __off, 4, __dbStmt);
  }
  public void readFields(DataInput __dataIn) throws IOException {
this.readFields0(__dataIn);  }
  public void readFields0(DataInput __dataIn) throws IOException {
    if (__dataIn.readBoolean()) { 
        this.GRADE_ID = null;
    } else {
    this.GRADE_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.SCORE = null;
    } else {
    this.SCORE = com.cloudera.sqoop.lib.BigDecimalSerializer.readFields(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.ASSESSMENT_ID = null;
    } else {
    this.ASSESSMENT_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.TRAINEE_ID = null;
    } else {
    this.TRAINEE_ID = Integer.valueOf(__dataIn.readInt());
    }
  }
  public void write(DataOutput __dataOut) throws IOException {
    if (null == this.GRADE_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.GRADE_ID);
    }
    if (null == this.SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.SCORE, __dataOut);
    }
    if (null == this.ASSESSMENT_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.ASSESSMENT_ID);
    }
    if (null == this.TRAINEE_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TRAINEE_ID);
    }
  }
  public void write0(DataOutput __dataOut) throws IOException {
    if (null == this.GRADE_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.GRADE_ID);
    }
    if (null == this.SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.SCORE, __dataOut);
    }
    if (null == this.ASSESSMENT_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.ASSESSMENT_ID);
    }
    if (null == this.TRAINEE_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TRAINEE_ID);
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
    __sb.append(FieldFormatter.escapeAndEnclose(GRADE_ID==null?"null":"" + GRADE_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(SCORE==null?"null":SCORE.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_ID==null?"null":"" + ASSESSMENT_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TRAINEE_ID==null?"null":"" + TRAINEE_ID, delimiters));
    if (useRecordDelim) {
      __sb.append(delimiters.getLinesTerminatedBy());
    }
    return __sb.toString();
  }
  public void toString0(DelimiterSet delimiters, StringBuilder __sb, char fieldDelim) {
    __sb.append(FieldFormatter.escapeAndEnclose(GRADE_ID==null?"null":"" + GRADE_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(SCORE==null?"null":SCORE.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_ID==null?"null":"" + ASSESSMENT_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TRAINEE_ID==null?"null":"" + TRAINEE_ID, delimiters));
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
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.GRADE_ID = null; } else {
      this.GRADE_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.SCORE = null; } else {
      this.SCORE = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.ASSESSMENT_ID = null; } else {
      this.ASSESSMENT_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TRAINEE_ID = null; } else {
      this.TRAINEE_ID = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  private void __loadFromFields0(Iterator<String> __it) {
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.GRADE_ID = null; } else {
      this.GRADE_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.SCORE = null; } else {
      this.SCORE = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.ASSESSMENT_ID = null; } else {
      this.ASSESSMENT_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TRAINEE_ID = null; } else {
      this.TRAINEE_ID = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  public Object clone() throws CloneNotSupportedException {
    BATTERY_QUANTITATIVE o = (BATTERY_QUANTITATIVE) super.clone();
    return o;
  }

  public void clone0(BATTERY_QUANTITATIVE o) throws CloneNotSupportedException {
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> __sqoop$field_map = new HashMap<String, Object>();
    __sqoop$field_map.put("GRADE_ID", this.GRADE_ID);
    __sqoop$field_map.put("SCORE", this.SCORE);
    __sqoop$field_map.put("ASSESSMENT_ID", this.ASSESSMENT_ID);
    __sqoop$field_map.put("TRAINEE_ID", this.TRAINEE_ID);
    return __sqoop$field_map;
  }

  public void getFieldMap0(Map<String, Object> __sqoop$field_map) {
    __sqoop$field_map.put("GRADE_ID", this.GRADE_ID);
    __sqoop$field_map.put("SCORE", this.SCORE);
    __sqoop$field_map.put("ASSESSMENT_ID", this.ASSESSMENT_ID);
    __sqoop$field_map.put("TRAINEE_ID", this.TRAINEE_ID);
  }

  public void setField(String __fieldName, Object __fieldVal) {
    if (!setters.containsKey(__fieldName)) {
      throw new RuntimeException("No such field:"+__fieldName);
    }
    setters.get(__fieldName).setField(__fieldVal);
  }

}
