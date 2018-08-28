import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException {
        FileReader filereader = new FileReader("src/main/resources/C2ImportCalEventSample.csv");
        CSVReader csvReader = new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .build();
        List<String[]> allData = csvReader.readAll();
        Schema schema = new Schema.Parser().parse(new File("src/main/resources/user.avsc"));
        File file = new File("src/main/resources/users.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        for (String[] row : allData) {
            GenericRecord user1 = new GenericData.Record(schema);
            user1.put("EmployeeID", Integer.parseInt(row[0]));
            user1.put("StartTime", row[1]);
            user1.put("EndDate", row[2]);
            user1.put("EndTime", row[3]);
            user1.put("EventTitle", row[4]);
            user1.put("AllDayEvent", row[5]);
            user1.put("NoEndTime", row[6]);
            user1.put("EventDescription", row[7]);
            dataFileWriter.append(user1);
        }
        dataFileWriter.close();
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}