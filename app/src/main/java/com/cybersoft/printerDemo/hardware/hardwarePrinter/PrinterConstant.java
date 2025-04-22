package com.cybersoft.printerDemo.hardware.hardwarePrinter;

import com.pax.gl.page.IPage;

public class PrinterConstant {
    public static final IPage.EAlign LEFT = IPage.EAlign.LEFT;
    public static final IPage.EAlign CENTER = IPage.EAlign.CENTER;
    public static final IPage.EAlign RIGHT = IPage.EAlign.RIGHT;
    public static final int LINE_SPACE = 10;
    public static final int LINE_SPACE_S = 5;
    public static final int LINE_SPACE_M = 20;
    public static final int LINE_SPACE_L = 30;
    public static final int LINE_SPACE_XL = 40;
    public static final int LINE_SPACE_XXL = 60;
    public static final int LINE_SPACE_BLOCK = 150;
    public static final int PRINT_TAIL_BLOCK = 300;
    public static final int TXT_XS = 14;
    public static final int TXT_S = 16;
    public static final int TXT_SS = 18;
    public static final int TXT_MM = 21;
    public static final int TXT_M = 22;
    public static final int TXT_L = 26;
    public static final int TXT_XL = 28;
    public static final int TXT_29 = 29;
    public static final int TXT_XXL = 30;
    public static final int TXT_XXXL = 38;
    public static final int TXT_BOLD = IPage.ILine.IUnit.TEXT_STYLE_BOLD;
    public static final int TXT_NORMAL = IPage.ILine.IUnit.TEXT_STYLE_NORMAL;
    public static final int TXT_UNDERLINE = IPage.ILine.IUnit.TEXT_STYLE_UNDERLINE;
    public static final int ADJUST_LINE_SPACE = -6;
    public static final int LINE_SPACE_PARAM = -2;
    public static final int PAGE_WIDTH = 384;
    public static final float FLOAT_2 = 0.2f;
    public static final float FLOAT_25 = 0.25f;
    public static final float FLOAT_3 = 0.3f;
    public static final float FLOAT_35 = 0.35f;
    public static final float FLOAT_4 = 0.4f;
    public static final float FLOAT_5 = 0.5f;
    public static final float FLOAT_6 = 0.6f;
    public static final float FLOAT_7 = 0.7f;
    public static final float FLOAT_8 = 0.8f;
    public static final float FLOAT_9 = 0.9f;
    public static final String SEPARATE_PARAM = "==============================================";
    public static final String SEPARATE_MID = "================================";
    public static final String SEPARATE = "=============";
    public static final String SEPARATE_ISSUER = "***";
    public static final String SEPARATE_NORMAL2 = "────────────────────────";   // 24個剛好整條
    public static final String SEPARATE_NORMAL = "————————————————————"; //20個
    public static final String SEPARATE_STAR = "****************************";
    public static final String UNDERLINE_TIP = "————————————";
    public static final String UNDERLINE_TOTAL = "———————————";
    public static final int GRAY = 3;
    public static final int CUT_PAPER_CUT_ALL = 0;
    public static final int CUT_PAPER_CUT_PART = 1;
    public static final String REDEEM = "REDEEM";
    public static final String IPP = "IPP";

    //列印的LOGO
    //public static final String TIS_TSB_LOGO = "B9123CA93CA041F3A17516B8773E544D";

    //列印用字形，預設 "/system/fonts/DroidSansFallback.ttf"
    //public static final String PRINTER_FONT = "/data/resource/font/YaHei_Consolas_Hybrid_1.12.ttf";

    // 列印用
    public static final String PRINTER_FONT = getPrinterFont();//"/system/fonts/DroidSansFallback.ttf";

    // Smilee+@191210 A920字型會在system層
    // 用PayDroid tool丟進去機器裡/data/resource/font裡
    private static String getPrinterFont() {
//        File file;
//        file = new File("/system/fonts/DroidSansFallback.ttf");
//        if (file.exists()) {
//            return "/system/fonts/DroidSansFallback.ttf";
//        }
//        // PayDroid tool會丟進這裡
//        file = new File("/data/resource/font/DroidSansFallback.ttf");
//        if (file.exists()) {
//            return "/data/resource/font/DroidSansFallback.ttf";
//        }
        return "/system/fonts/DroidSansFallback.ttf";
    }

    public enum PrintResultCode {
        SUCCESS(0, "列印成功"),
        BUSY(1, "列印機忙碌中"),
        NO_PAPER(2, "缺紙"),
        FORMAT_ERROR(3, "格式錯誤"),
        MALFUNCTION(4, "機器故障"),
        OVERHEAT(5, "過熱"),
        NO_POWER(6, "未連接電源"),
        UNFINISHED(7, "列印未完成"),
        NO_FONT(8, "缺字型"),
        DATA_TOO_LONG(9, "資料過長"),
        NO_DATA_PRINT(997,"打印資料為空"),
        DEVICE_CANT_INIT(998,"設備初始化失敗"),
        UNKNOWN(999, "未知錯誤");


        private final int code;
        private final String description;

        PrintResultCode(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }



    // 用來列印總額/結帳報表的各項筆數加總Sub Title
    public enum StatisticsList {
        TOTAL("總計"),
        REDEEM_TOTAL("紅利總計"),
        IPP_TOTAL("分期總計"),
        ACTUAL_AMOUNT("實際交易金額總計"),
        REDEEM_AMOUNT("折抵金額總計"),
        REDEEM_POINT("折抵點數總計"),
        SALE("銷售"),
        REDEEM_SALE("紅利"),
        IPP_SALE("分期"),
        REFUND("退貨"),
        REDEEM_REFUND("紅利退貨"),
        IPP_REFUND("分期退貨"),
        TIPS("小費"),
        VOID_SALE("取消銷售"),
        VOID_REFUND("取消退貨"),
        IPP_REFUND_PERIOD("分期退貨"),
        IPP_PERIOD("分期");

        private final String alias;

        StatisticsList(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return this.alias;
        }
    }

    public enum FontStyle {
        SMALL,
        MEDIUM,
        LARGE,
        EXTRA_LARGE
    }
}
