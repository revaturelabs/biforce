# Development

To build locally:

```bash
mvn package -T 1C
```

## Run Biforce
To run Biforce, you must first get the input csv file from your project manager.

To Run in a cluster:
```bash
spark-submit --master local[4] --class com.revature.MLDriver <jar> <input_csv_file> <output_dir>
```
