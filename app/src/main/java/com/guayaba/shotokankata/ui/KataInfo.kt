package com.guayaba.shotokankata.ui

enum class KataInfo(val id: Int, val kataName: String, val japaneseName: String) {
    HEIAN_SHODAN(1, "Heian Shodan", "平安初段"),
    HEIAN_NIDAN(2, "Heian Nidan", "平安二段"),
    HEIAN_SANDAN(3, "Heian Sandan", "平安三段"),
    HEIAN_YONDAN(4, "Heian Yondan", "平安四段"),
    HEIAN_GODAN(5, "Heian Godan", "平安五段"),
    TEKKI_SHODAN(6, "Tekki Shodan", "鉄騎初段"),
    BASSAI_DAI(7, "Bassai-dai", "披塞大"),
    JION(8, "Jion", "慈恩"),
    KANKU_DAI(9, "Kanku-dai", "観空大"),
    ENPI(10, "Enpi", "燕飛"),
    HANGETSU(11, "Hangetsu", "半月"),
    TEKKI_NIDAN(12, "Tekki Nidan", "鉄騎二段"),
    TEKKI_SANDAN(13, "Tekki Sandan", "鉄騎三段"),
    BASSAI_SHO(14, "Bassai-sho", "披塞小"),
    JITTE(15, "Jitte", "十手"),
    GANKAKU(16, "Gankaku", "岩鶴"),
    KANKU_SHO(17, "Kanku-sho", "観空小"),
    NIJUSHIHO(18, "Nijushiho", "二十四步"),
    CHINTE(19, "Chinte", "珍手"),
    UNSU(20, "Unsu", "雲手"),
    SOCHIN(21, "Sochin", "壯鎭"),
    GOJUSHIHO_DAI(22, "Gojushiho-dai", "五十四歩大"),
    GOJUSHIHO_SHO(23, "Gojushijo-sho", "五十四歩小"),
    MEIKYO(24, "Meikyo", "明鏡"),
    WANKAN(25, "Wankan", "王冠"),
    JIIN(26, "Ji'in", "慈陰");

    companion object {
        fun findById(id: Int) = KataInfo.entries.find { it.id == id }
    }
}