To run the program, it needs a value passed from command line as args[0].

args[0] is the output file, which is the location used in writeToProtoBuf(location) function

Also, please change the input csv file path in init(). 

You may need protobuf-java-[version].jar to run writeToProtoBuf(location) function

The .jar file is in src/csv/, just add it into project build path.