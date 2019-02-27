// ORM class for table 'BATTERY_ASSESSMENT'
// WARNING: This class is AUTO-GENERATED. Modify at your own risk.
//
// Debug information:
// Generated date: Thu Feb 07 16:46:40 PST 2019
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

public class BATTERY_ASSESSMENT extends SqoopRecord  implements DBWritable, Writable {
  private final int PROTOCOL_VERSION = 3;
  public int getClassFormatVersion() { return PROTOCOL_VERSION; }
  public static interface FieldSetterCommand {    void setField(Object value);  }  protected ResultSet __cur_result_set;
  private Map<String, FieldSetterCommand> setters = new HashMap<String, FieldSetterCommand>();
  private void init0() {
    setters.put("ASSESSMENT_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        ASSESSMENT_ID = (Integer)value;
      }
    });
    setters.put("RAW_SCORE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        RAW_SCORE = (Integer)value;
      }
    });
    setters.put("ASSESSMENT_TYPE", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        ASSESSMENT_TYPE = (String)value;
      }
    });
    setters.put("WEEK_NUMBER", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        WEEK_NUMBER = (Integer)value;
      }
    });
    setters.put("BATCH_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        BATCH_ID = (Integer)value;
      }
    });
    setters.put("ASSESSMENT_CATEGORY", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        ASSESSMENT_CATEGORY = (Integer)value;
      }
    });
  }
  public BATTERY_ASSESSMENT() {
    init0();
  }
  private Integer ASSESSMENT_ID;
  public Integer get_ASSESSMENT_ID() {
    return ASSESSMENT_ID;
  }
  public void set_ASSESSMENT_ID(Integer ASSESSMENT_ID) {
    this.ASSESSMENT_ID = ASSESSMENT_ID;
  }
  public BATTERY_ASSESSMENT with_ASSESSMENT_ID(Integer ASSESSMENT_ID) {
    this.ASSESSMENT_ID = ASSESSMENT_ID;
    return this;
  }
  private Integer RAW_SCORE;
  public Integer get_RAW_SCORE() {
    return RAW_SCORE;
  }
  public void set_RAW_SCORE(Integer RAW_SCORE) {
    this.RAW_SCORE = RAW_SCORE;
  }
  public BATTERY_ASSESSMENT with_RAW_SCORE(Integer RAW_SCORE) {
    this.RAW_SCORE = RAW_SCORE;
    return this;
  }
  private String ASSESSMENT_TYPE;
  public String get_ASSESSMENT_TYPE() {
    return ASSESSMENT_TYPE;
  }
  public void set_ASSESSMENT_TYPE(String ASSESSMENT_TYPE) {
    this.ASSESSMENT_TYPE = ASSESSMENT_TYPE;
  }
  public BATTERY_ASSESSMENT with_ASSESSMENT_TYPE(String ASSESSMENT_TYPE) {
    this.ASSESSMENT_TYPE = ASSESSMENT_TYPE;
    return this;
  }
  private Integer WEEK_NUMBER;
  public Integer get_WEEK_NUMBER() {
    return WEEK_NUMBER;
  }
  public void set_WEEK_NUMBER(Integer WEEK_NUMBER) {
    this.WEEK_NUMBER = WEEK_NUMBER;
  }
  public BATTERY_ASSESSMENT with_WEEK_NUMBER(Integer WEEK_NUMBER) {
    this.WEEK_NUMBER = WEEK_NUMBER;
    return this;
  }
  private Integer BATCH_ID;
  public Integer get_BATCH_ID() {
    return BATCH_ID;
  }
  public void set_BATCH_ID(Integer BATCH_ID) {
    this.BATCH_ID = BATCH_ID;
  }
  public BATTERY_ASSESSMENT with_BATCH_ID(Integer BATCH_ID) {
    this.BATCH_ID = BATCH_ID;
    return this;
  }
  private Integer ASSESSMENT_CATEGORY;
  public Integer get_ASSESSMENT_CATEGORY() {
    return ASSESSMENT_CATEGORY;
  }
  public void set_ASSESSMENT_CATEGORY(Integer ASSESSMENT_CATEGORY) {
    this.ASSESSMENT_CATEGORY = ASSESSMENT_CATEGORY;
  }
  public BATTERY_ASSESSMENT with_ASSESSMENT_CATEGORY(Integer ASSESSMENT_CATEGORY) {
    this.ASSESSMENT_CATEGORY = ASSESSMENT_CATEGORY;
    return this;
  }
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY_ASSESSMENT)) {
      return false;
    }
    BATTERY_ASSESSMENT that = (BATTERY_ASSESSMENT) o;
    boolean equal = true;
    equal = equal && (this.ASSESSMENT_ID == null ? that.ASSESSMENT_ID == null : this.ASSESSMENT_ID.equals(that.ASSESSMENT_ID));
    equal = equal && (this.RAW_SCORE == null ? that.RAW_SCORE == null : this.RAW_SCORE.equals(that.RAW_SCORE));
    equal = equal && (this.ASSESSMENT_TYPE == null ? that.ASSESSMENT_TYPE == null : this.ASSESSMENT_TYPE.equals(that.ASSESSMENT_TYPE));
    equal = equal && (this.WEEK_NUMBER == null ? that.WEEK_NUMBER == null : this.WEEK_NUMBER.equals(that.WEEK_NUMBER));
    equal = equal && (this.BATCH_ID == null ? that.BATCH_ID == null : this.BATCH_ID.equals(that.BATCH_ID));
    equal = equal && (this.ASSESSMENT_CATEGORY == null ? that.ASSESSMENT_CATEGORY == null : this.ASSESSMENT_CATEGORY.equals(that.ASSESSMENT_CATEGORY));
    return equal;
  }
  public boolean equals0(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY_ASSESSMENT)) {
      return false;
    }
    BATTERY_ASSESSMENT that = (BATTERY_ASSESSMENT) o;
    boolean equal = true;
    equal = equal && (this.ASSESSMENT_ID == null ? that.ASSESSMENT_ID == null : this.ASSESSMENT_ID.equals(that.ASSESSMENT_ID));
    equal = equal && (this.RAW_SCORE == null ? that.RAW_SCORE == null : this.RAW_SCORE.equals(that.RAW_SCORE));
    equal = equal && (this.ASSESSMENT_TYPE == null ? that.ASSESSMENT_TYPE == null : this.ASSESSMENT_TYPE.equals(that.ASSESSMENT_TYPE));
    equal = equal && (this.WEEK_NUMBER == null ? that.WEEK_NUMBER == null : this.WEEK_NUMBER.equals(that.WEEK_NUMBER));
    equal = equal && (this.BATCH_ID == null ? that.BATCH_ID == null : this.BATCH_ID.equals(that.BATCH_ID));
    equal = equal && (this.ASSESSMENT_CATEGORY == null ? that.ASSESSMENT_CATEGORY == null : this.ASSESSMENT_CATEGORY.equals(that.ASSESSMENT_CATEGORY));
    return equal;
  }
  public void readFields(ResultSet __dbResults) throws SQLException {
    this.__cur_result_set = __dbResults;
    this.ASSESSMENT_ID = JdbcWritableBridge.readInteger(1, __dbResults);
    this.RAW_SCORE = JdbcWritableBridge.readInteger(2, __dbResults);
    this.ASSESSMENT_TYPE = JdbcWritableBridge.readString(3, __dbResults);
    this.WEEK_NUMBER = JdbcWritableBridge.readInteger(4, __dbResults);
    this.BATCH_ID = JdbcWritableBridge.readInteger(5, __dbResults);
    this.ASSESSMENT_CATEGORY = JdbcWritableBridge.readInteger(6, __dbResults);
  }
  public void readFields0(ResultSet __dbResults) throws SQLException {
    this.ASSESSMENT_ID = JdbcWritableBridge.readInteger(1, __dbResults);
    this.RAW_SCORE = JdbcWritableBridge.readInteger(2, __dbResults);
    this.ASSESSMENT_TYPE = JdbcWritableBridge.readString(3, __dbResults);
    this.WEEK_NUMBER = JdbcWritableBridge.readInteger(4, __dbResults);
    this.BATCH_ID = JdbcWritableBridge.readInteger(5, __dbResults);
    this.ASSESSMENT_CATEGORY = JdbcWritableBridge.readInteger(6, __dbResults);
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
    JdbcWritableBridge.writeInteger(ASSESSMENT_ID, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(RAW_SCORE, 2 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeString(ASSESSMENT_TYPE, 3 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeInteger(WEEK_NUMBER, 4 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BATCH_ID, 5 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(ASSESSMENT_CATEGORY, 6 + __off, 4, __dbStmt);
    return 6;
  }
  public void write0(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(ASSESSMENT_ID, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(RAW_SCORE, 2 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeString(ASSESSMENT_TYPE, 3 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeInteger(WEEK_NUMBER, 4 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(BATCH_ID, 5 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeInteger(ASSESSMENT_CATEGORY, 6 + __off, 4, __dbStmt);
  }
  public void readFields(DataInput __dataIn) throws IOException {
this.readFields0(__dataIn);  }
  public void readFields0(DataInput __dataIn) throws IOException {
    if (__dataIn.readBoolean()) { 
        this.ASSESSMENT_ID = null;
    } else {
    this.ASSESSMENT_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.RAW_SCORE = null;
    } else {
    this.RAW_SCORE = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.ASSESSMENT_TYPE = null;
    } else {
    this.ASSESSMENT_TYPE = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.WEEK_NUMBER = null;
    } else {
    this.WEEK_NUMBER = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.BATCH_ID = null;
    } else {
    this.BATCH_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.ASSESSMENT_CATEGORY = null;
    } else {
    this.ASSESSMENT_CATEGORY = Integer.valueOf(__dataIn.readInt());
    }
  }
  public void write(DataOutput __dataOut) throws IOException {
    if (null == this.ASSESSMENT_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.ASSESSMENT_ID);
    }
    if (null == this.RAW_SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.RAW_SCORE);
    }
    if (null == this.ASSESSMENT_TYPE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, ASSESSMENT_TYPE);
    }
    if (null == this.WEEK_NUMBER) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.WEEK_NUMBER);
    }
    if (null == this.BATCH_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATCH_ID);
    }
    if (null == this.ASSESSMENT_CATEGORY) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.ASSESSMENT_CATEGORY);
    }
  }
  public void write0(DataOutput __dataOut) throws IOException {
    if (null == this.ASSESSMENT_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.ASSESSMENT_ID);
    }
    if (null == this.RAW_SCORE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.RAW_SCORE);
    }
    if (null == this.ASSESSMENT_TYPE) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, ASSESSMENT_TYPE);
    }
    if (null == this.WEEK_NUMBER) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.WEEK_NUMBER);
    }
    if (null == this.BATCH_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATCH_ID);
    }
    if (null == this.ASSESSMENT_CATEGORY) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.ASSESSMENT_CATEGORY);
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
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_ID==null?"null":"" + ASSESSMENT_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(RAW_SCORE==null?"null":"" + RAW_SCORE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_TYPE==null?"null":ASSESSMENT_TYPE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(WEEK_NUMBER==null?"null":"" + WEEK_NUMBER, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATCH_ID==null?"null":"" + BATCH_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_CATEGORY==null?"null":"" + ASSESSMENT_CATEGORY, delimiters));
    if (useRecordDelim) {
      __sb.append(delimiters.getLinesTerminatedBy());
    }
    return __sb.toString();
  }
  public void toString0(DelimiterSet delimiters, StringBuilder __sb, char fieldDelim) {
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_ID==null?"null":"" + ASSESSMENT_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(RAW_SCORE==null?"null":"" + RAW_SCORE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_TYPE==null?"null":ASSESSMENT_TYPE, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(WEEK_NUMBER==null?"null":"" + WEEK_NUMBER, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATCH_ID==null?"null":"" + BATCH_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(ASSESSMENT_CATEGORY==null?"null":"" + ASSESSMENT_CATEGORY, delimiters));
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
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.ASSESSMENT_ID = null; } else {
      this.ASSESSMENT_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.RAW_SCORE = null; } else {
      this.RAW_SCORE = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.ASSESSMENT_TYPE = null; } else {
      this.ASSESSMENT_TYPE = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.WEEK_NUMBER = null; } else {
      this.WEEK_NUMBER = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATCH_ID = null; } else {
      this.BATCH_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.ASSESSMENT_CATEGORY = null; } else {
      this.ASSESSMENT_CATEGORY = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  private void __loadFromFields0(Iterator<String> __it) {
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.ASSESSMENT_ID = null; } else {
      this.ASSESSMENT_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.RAW_SCORE = null; } else {
      this.RAW_SCORE = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.ASSESSMENT_TYPE = null; } else {
      this.ASSESSMENT_TYPE = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.WEEK_NUMBER = null; } else {
      this.WEEK_NUMBER = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATCH_ID = null; } else {
      this.BATCH_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.ASSESSMENT_CATEGORY = null; } else {
      this.ASSESSMENT_CATEGORY = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  public Object clone() throws CloneNotSupportedException {
    BATTERY_ASSESSMENT o = (BATTERY_ASSESSMENT) super.clone();
    return o;
  }

  public void clone0(BATTERY_ASSESSMENT o) throws CloneNotSupportedException {
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> __sqoop$field_map = new HashMap<String, Object>();
    __sqoop$field_map.put("ASSESSMENT_ID", this.ASSESSMENT_ID);
    __sqoop$field_map.put("RAW_SCORE", this.RAW_SCORE);
    __sqoop$field_map.put("ASSESSMENT_TYPE", this.ASSESSMENT_TYPE);
    __sqoop$field_map.put("WEEK_NUMBER", this.WEEK_NUMBER);
    __sqoop$field_map.put("BATCH_ID", this.BATCH_ID);
    __sqoop$field_map.put("ASSESSMENT_CATEGORY", this.ASSESSMENT_CATEGORY);
    return __sqoop$field_map;
  }

  public void getFieldMap0(Map<String, Object> __sqoop$field_map) {
    __sqoop$field_map.put("ASSESSMENT_ID", this.ASSESSMENT_ID);
    __sqoop$field_map.put("RAW_SCORE", this.RAW_SCORE);
    __sqoop$field_map.put("ASSESSMENT_TYPE", this.ASSESSMENT_TYPE);
    __sqoop$field_map.put("WEEK_NUMBER", this.WEEK_NUMBER);
    __sqoop$field_map.put("BATCH_ID", this.BATCH_ID);
    __sqoop$field_map.put("ASSESSMENT_CATEGORY", this.ASSESSMENT_CATEGORY);
  }

  public void setField(String __fieldName, Object __fieldVal) {
    if (!setters.containsKey(__fieldName)) {
      throw new RuntimeException("No such field:"+__fieldName);
    }
    setters.get(__fieldName).setField(__fieldVal);
  }

}
