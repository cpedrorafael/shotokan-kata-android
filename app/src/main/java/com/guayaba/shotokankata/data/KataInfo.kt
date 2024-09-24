package com.guayaba.shotokankata.data

enum class KataInfo(val id: Int, val kataName: String, val japaneseName: String, val moves: Int) {
    HEIAN_SHODAN(1, "Heian Shodan", "平安初段", 21),
    HEIAN_NIDAN(2, "Heian Nidan", "平安二段", 26),
    HEIAN_SANDAN(3, "Heian Sandan", "平安三段", 20),
    HEIAN_YONDAN(4, "Heian Yondan", "平安四段", 27),
    HEIAN_GODAN(5, "Heian Godan", "平安五段", 23),
    TEKKI_SHODAN(6, "Tekki Shodan", "鉄騎初段", 29),
    BASSAI_DAI(7, "Bassai-dai", "披塞大", 42),
    JION(8, "Jion", "慈恩", 47),
    KANKU_DAI(9, "Kanku-dai", "観空大", 65),
    ENPI(10, "Enpi", "燕飛", 37),
    HANGETSU(11, "Hangetsu", "半月", 41),
    TEKKI_NIDAN(12, "Tekki Nidan", "鉄騎二段", 24),
    TEKKI_SANDAN(13, "Tekki Sandan", "鉄騎三段", 36),
    BASSAI_SHO(14, "Bassai-sho", "披塞小", 27),
    JITTE(15, "Jitte", "十手", 24),
    GANKAKU(16, "Gankaku", "岩鶴", 42),
    KANKU_SHO(17, "Kanku-sho", "観空小", 47),
    NIJUSHIHO(18, "Nijushiho", "二十四步", 33),
    CHINTE(19, "Chinte", "珍手", 33),
    UNSU(20, "Unsu", "雲手", 48),
    SOCHIN(21, "Sochin", "壯鎭", 40),
    GOJUSHIHO_DAI(22, "Gojushiho-dai", "五十四歩大", 62),
    GOJUSHIHO_SHO(23, "Gojushijo-sho", "五十四歩小", 65),
    MEIKYO(24, "Meikyo", "明鏡", 32),
    WANKAN(25, "Wankan", "王冠", 22),
    JIIN(26, "Ji'in", "慈陰", 35);

    companion object {
        fun findById(id: Int) = KataInfo.entries.find { it.id == id }
            ?: throw IllegalStateException("Kata not found for provided ID: $id")
    }
}