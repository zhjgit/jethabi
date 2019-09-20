package com.sumingk;

import com.sumingk.core.EncodeData;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class EthAbiEncode {


    public static void main(String[] args) {
        //-f setInfo -t bytes32 uint[] bool string -v 测试 [3,5] true 第一个demo
        //-f setInfo -t bytes32 uint bool string -v 测试 3 true 第一个demo
        // address 0x3ed1B4841fDcd6F6437e93Db7F5e2C9694664f78
        String[] local_args = null;
        if (args.length == 0) {
            local_args = new String[]{"-h"};
        } else {
            local_args = args;
        }
        //定义
        Options options = new Options();
        char separator = ' ';
        options.addOption("h", "help", false, "show jethabi all cmd options");//false代表不强制有
        options.addOption("e", "example", false, "e.g.: -f setInfo -t bytes32 uint256[] bool bytes string " +
                "-v liuyu [7,5] true dynamicinfo xxcityxxroad");

        /*options.addOption("f", "function", true, "contract function name");*/
        List<String> listArgs = Arrays.asList(local_args);
        boolean flag = !(listArgs.contains("-h") || listArgs.contains("--help") || listArgs.contains("-e"));

        options.addOption(Option.builder("f")
                .argName("arg").longOpt("function").required(flag).hasArg().desc("contract function name").build());

        //options.addOption("t", "type", true, "params type, multiple type Separator use ' '");
        options.addOption(Option.builder("t")
                .argName("arg").longOpt("type").required(false).hasArg().optionalArg(true).numberOfArgs(18).desc("params type, multiple type Separator use Spacing ")
                .valueSeparator(separator).build());

        //options.addOption("v", "value", true, "params value, multiple value same format as type param");
        options.addOption(Option.builder("v")
                .argName("arg").longOpt("value").required(false).hasArg().optionalArg(true).numberOfArgs(18).desc("params value, multiple value same format as type param," +
                        "If the value is empty, use ? instead")
                .valueSeparator(separator).build());

        //解析
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, local_args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String func = "";
        String[] types = null;
        String[] values = null;

        HelpFormatter hf = null;
        //查询交互
        if (cmd.hasOption("h") || cmd.hasOption("help")) {
            String formatstr = "\n java -jar jethabi.jar [-f] <arg> [-t] <arg> [-v] <arg> \n";
            hf = new HelpFormatter();
            hf.printHelp(formatstr, "Options:", options, "");
            return;
        }

        if (cmd.hasOption("e")) {
            hf = new HelpFormatter();
            Options ops = new Options();
            Option option = options.getOption("e");
            ops.addOption(option);
            hf.printHelp(option.getLongOpt(), ops);
            return;
        }

        if (cmd.hasOption("f")) {
            func = cmd.getOptionValue("f");
        }

        if (cmd.hasOption("t")) {
            types = cmd.getOptionValues("t");
            //System.out.println(Arrays.toString(types) + ",-length:" + types.length);
        }

        if (cmd.hasOption("v")) {
            values = cmd.getOptionValues("v");
            //System.out.println(Arrays.toString(values) + ",-length:" + values.length);
        }

        if ((null != types && null != values) && (types.length != values.length)) {
            try {
                throw new Exception("The lengths of '-t types' and '-v values' are not equal");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        EncodeData.outEncodeData(func, types, values);
    }
}
