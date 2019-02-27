// ORM class for table 'BATTERY'
// WARNING: This class is AUTO-GENERATED. Modify at your own risk.
//
// Debug information:
// Generated date: Thu Feb 07 16:39:10 PST 2019
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

public class BATTERY extends SqoopRecord  implements DBWritable, Writable {
  private final int PROTOCOL_VERSION = 3;
  public int getClassFormatVersion() { return PROTOCOL_VERSION; }
  public static interface FieldSetterCommand {    void setField(Object value);  }  protected ResultSet __cur_result_set;
  private Map<String, FieldSetterCommand> setters = new HashMap<String, FieldSetterCommand>();
  private void init0() {
    setters.put("TRAINEE_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        TRAINEE_ID = (Integer)value;
      }
    });
    setters.put("TRAINING_STATUS", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        TRAINING_STATUS = (String)value;
      }
    });
    setters.put("BATCH_ID", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        BATCH_ID = (Integer)value;
      }
    });
  }
  public BATTERY() {
    init0();
  }
  private Integer TRAINEE_ID;
  public Integer get_TRAINEE_ID() {
    return TRAINEE_ID;
  }
  public void set_TRAINEE_ID(Integer TRAINEE_ID) {
    this.TRAINEE_ID = TRAINEE_ID;
  }
  public BATTERY with_TRAINEE_ID(Integer TRAINEE_ID) {
    this.TRAINEE_ID = TRAINEE_ID;
    return this;
  }
  private String TRAINING_STATUS;
  public String get_TRAINING_STATUS() {
    return TRAINING_STATUS;
  }
  public void set_TRAINING_STATUS(String TRAINING_STATUS) {
    this.TRAINING_STATUS = TRAINING_STATUS;
  }
  public BATTERY with_TRAINING_STATUS(String TRAINING_STATUS) {
    this.TRAINING_STATUS = TRAINING_STATUS;
    return this;
  }
  private Integer BATCH_ID;
  public Integer get_BATCH_ID() {
    return BATCH_ID;
  }
  public void set_BATCH_ID(Integer BATCH_ID) {
    this.BATCH_ID = BATCH_ID;
  }
  public BATTERY with_BATCH_ID(Integer BATCH_ID) {
    this.BATCH_ID = BATCH_ID;
    return this;
  }
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY)) {
      return false;
    }
    BATTERY that = (BATTERY) o;
    boolean equal = true;
    equal = equal && (this.TRAINEE_ID == null ? that.TRAINEE_ID == null : this.TRAINEE_ID.equals(that.TRAINEE_ID));
    equal = equal && (this.TRAINING_STATUS == null ? that.TRAINING_STATUS == null : this.TRAINING_STATUS.equals(that.TRAINING_STATUS));
    equal = equal && (this.BATCH_ID == null ? that.BATCH_ID == null : this.BATCH_ID.equals(that.BATCH_ID));
    return equal;
  }
  public boolean equals0(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BATTERY)) {
      return false;
    }
    BATTERY that = (BATTERY) o;
    boolean equal = true;
    equal = equal && (this.TRAINEE_ID == null ? that.TRAINEE_ID == null : this.TRAINEE_ID.equals(that.TRAINEE_ID));
    equal = equal && (this.TRAINING_STATUS == null ? that.TRAINING_STATUS == null : this.TRAINING_STATUS.equals(that.TRAINING_STATUS));
    equal = equal && (this.BATCH_ID == null ? that.BATCH_ID == null : this.BATCH_ID.equals(that.BATCH_ID));
    return equal;
  }
  public void readFields(ResultSet __dbResults) throws SQLException {
    this.__cur_result_set = __dbResults;
    this.TRAINEE_ID = JdbcWritableBridge.readInteger(1, __dbResults);
    this.TRAINING_STATUS = JdbcWritableBridge.readString(2, __dbResults);
    this.BATCH_ID = JdbcWritableBridge.readInteger(3, __dbResults);
  }
  public void readFields0(ResultSet __dbResults) throws SQLException {
    this.TRAINEE_ID = JdbcWritableBridge.readInteger(1, __dbResults);
    this.TRAINING_STATUS = JdbcWritableBridge.readString(2, __dbResults);
    this.BATCH_ID = JdbcWritableBridge.readInteger(3, __dbResults);
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
    JdbcWritableBridge.writeInteger(TRAINEE_ID, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeString(TRAINING_STATUS, 2 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeInteger(BATCH_ID, 3 + __off, 4, __dbStmt);
    return 3;
  }
  public void write0(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(TRAINEE_ID, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeString(TRAINING_STATUS, 2 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeInteger(BATCH_ID, 3 + __off, 4, __dbStmt);
  }
  public void readFields(DataInput __dataIn) throws IOException {
this.readFields0(__dataIn);  }
  public void readFields0(DataInput __dataIn) throws IOException {
    if (__dataIn.readBoolean()) { 
        this.TRAINEE_ID = null;
    } else {
    this.TRAINEE_ID = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.TRAINING_STATUS = null;
    } else {
    this.TRAINING_STATUS = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.BATCH_ID = null;
    } else {
    this.BATCH_ID = Integer.valueOf(__dataIn.readInt());
    }
  }
  public void write(DataOutput __dataOut) throws IOException {
    if (null == this.TRAINEE_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TRAINEE_ID);
    }
    if (null == this.TRAINING_STATUS) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, TRAINING_STATUS);
    }
    if (null == this.BATCH_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATCH_ID);
    }
  }
  public void write0(DataOutput __dataOut) throws IOException {
    if (null == this.TRAINEE_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.TRAINEE_ID);
    }
    if (null == this.TRAINING_STATUS) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, TRAINING_STATUS);
    }
    if (null == this.BATCH_ID) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.BATCH_ID);
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
    __sb.append(FieldFormatter.escapeAndEnclose(TRAINEE_ID==null?"null":"" + TRAINEE_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TRAINING_STATUS==null?"null":TRAINING_STATUS, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATCH_ID==null?"null":"" + BATCH_ID, delimiters));
    if (useRecordDelim) {
      __sb.append(delimiters.getLinesTerminatedBy());
    }
    return __sb.toString();
  }
  public void toString0(DelimiterSet delimiters, StringBuilder __sb, char fieldDelim) {
    __sb.append(FieldFormatter.escapeAndEnclose(TRAINEE_ID==null?"null":"" + TRAINEE_ID, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(TRAINING_STATUS==null?"null":TRAINING_STATUS, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(BATCH_ID==null?"null":"" + BATCH_ID, delimiters));
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
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TRAINEE_ID = null; } else {
      this.TRAINEE_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.TRAINING_STATUS = null; } else {
      this.TRAINING_STATUS = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATCH_ID = null; } else {
      this.BATCH_ID = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  private void __loadFromFields0(Iterator<String> __it) {
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.TRAINEE_ID = null; } else {
      this.TRAINEE_ID = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.TRAINING_STATUS = null; } else {
      this.TRAINING_STATUS = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.BATCH_ID = null; } else {
      this.BATCH_ID = Integer.valueOf(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  public Object clone() throws CloneNotSupportedException {
    BATTERY o = (BATTERY) super.clone();
    return o;
  }

  public void clone0(BATTERY o) throws CloneNotSupportedException {
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> __sqoop$field_map = new HashMap<String, Object>();
    __sqoop$field_map.put("TRAINEE_ID", this.TRAINEE_ID);
    __sqoop$field_map.put("TRAINING_STATUS", this.TRAINING_STATUS);
    __sqoop$field_map.put("BATCH_ID", this.BATCH_ID);
    return __sqoop$field_map;
  }

  public void getFieldMap0(Map<String, Object> __sqoop$field_map) {
    __sqoop$field_map.put("TRAINEE_ID", this.TRAINEE_ID);
    __sqoop$field_map.put("TRAINING_STATUS", this.TRAINING_STATUS);
    __sqoop$field_map.put("BATCH_ID", this.BATCH_ID);
  }

  public void setField(String __fieldName, Object __fieldVal) {
    if (!setters.containsKey(__fieldName)) {
      throw new RuntimeException("No such field:"+__fieldName);
    }
    setters.get(__fieldName).setField(__fieldVal);
  }

}
