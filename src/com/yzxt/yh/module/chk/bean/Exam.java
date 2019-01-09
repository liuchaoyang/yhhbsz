package com.yzxt.yh.module.chk.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Exam implements Serializable
{
    private static final long serialVersionUID = -9183126564990950550L;
    private String id;
    private String custId;
    // 体检表编号
    private String examNo;
    @SerializedName("userName")
    private String name;
    @SerializedName("healthCheckTime")
    private Date examDate;
    private transient String symptom;
    @SerializedName("symptomOther")
    private String otherSymptom;
    @SerializedName("tempe")
    private Double temperature;
    @SerializedName("pulse")
    private Integer pulseRate;
    @SerializedName("breathingRate")
    private Integer breatheRate;
    @SerializedName("bpSysLeft")
    private Integer LBloodPressure;
    // 左侧舒张压
    @SerializedName("bpDiaLeft")
    private Integer LBloodPressureBpd;
    @SerializedName("bpSysRight")
    private Integer RBloodPressure;
    // 右侧舒张压
    @SerializedName("bpDiaRight")
    private Integer RBloodPressureBpd;
    private Integer height;
    private Double weight;
    @SerializedName("waist")
    private Integer waistline;
    @SerializedName("BMI")
    private Double bmi;
    @SerializedName("healthAss")
    private Integer agedHealthAssess;
    @SerializedName("selfcareAss")
    private Integer agedLifeAssess;
    @SerializedName("cognitiveFunction")
    private Integer agedKonwledgeFunc;
    @SerializedName("cognitiveFunctionScore")
    private Integer agedKfScore;
    @SerializedName("emotionalState")
    private Integer agedFeelingStatus;
    @SerializedName("emotionalStateScore")
    private Integer agedFsScore;
    @SerializedName("trainFrency")
    private Integer exerciseRate;
    @SerializedName("trainOnceTime")
    private Integer everyExerTime;
    @SerializedName("trainedTime")
    private Double insistExerYear;
    @SerializedName("trainMethod")
    private String exerType;
    @SerializedName("foodHabit")
    private transient String dietStatus;
    @SerializedName("smoking")
    private Integer smokeStatus;
    @SerializedName("smokingCountOneday")
    private Integer dailySmoke;
    @SerializedName("startSmokingAge")
    private Integer startSmokeAge;
    @SerializedName("stopSmokingAge")
    private Integer notSmokeAge;
    @SerializedName("drinkingFrency")
    private Integer drinkWineRate;
    @SerializedName("drinkingCountOneday")
    private Integer dailyDrink;
    @SerializedName("isStopDrinking")
    private Integer isNotDrink;
    @SerializedName("isStopDrinkingAge")
    private Integer notDrinkAge;
    @SerializedName("startDrinkingAge")
    private Integer startDrinkAge;
    @SerializedName("isOverDrinking")
    private Integer recentYearDrink;
    @SerializedName("drinkingType")
    private transient String drinkType;
    @SerializedName("drinkingTypeOther")
    private String otherDrinkType;
    private String workType;
    private Double workTime;
    @SerializedName("mouth")
    private Integer lips;
    private Integer tooth;
    @SerializedName("yan")
    private Integer throad;
    @SerializedName("leftEye")
    private Double LEyesight;
    @SerializedName("rightEye")
    private Double REyesight;
    @SerializedName("leftEyeChange")
    private Double reLEyesight;
    @SerializedName("rightEyeChang")
    private Double reREyesight;
    @SerializedName("listener")
    private Integer hearing;
    @SerializedName("sportWell")
    private Integer sportsFunc;
    @SerializedName("eyeBottom")
    private String eyeGround;
    private Integer skin;
    @SerializedName("skinOther")
    private String otherSkin;
    @SerializedName("gongmo")
    private Integer sclera;
    @SerializedName("gongmoOther")
    private String otherSclera;
    @SerializedName("linbajie")
    private Integer lymphaden;
    @SerializedName("llinbajieQita")
    private String otherLymphaden;
    @SerializedName("feiTongzhuang")
    private Integer lungsChest;
    @SerializedName("feiHuxi")
    private String lungsBreVoice;
    @SerializedName("feiLuoyin")
    private Integer lungsRale;
    @SerializedName("feiLuoyinYichang")
    private String otherLungsRale;
    @SerializedName("xinlv")
    private Integer heartRate;
    @SerializedName("xinlvJieguo")
    private Integer heartRhythm;
    @SerializedName("xinZayin")
    private String heartNoise;
    @SerializedName("fubuYatong")
    private String abdPrePain;
    @SerializedName("fubuBokuai")
    private String abdBagPiece;
    @SerializedName("ganda")
    private String abdHepatomegaly;
    @SerializedName("pida")
    private String abdSplenomegaly;
    @SerializedName("yidongxingzhuo")
    private String abdMovNoise;
    @SerializedName("xiazhiShuizhong")
    private Integer limbsEdema;
    @SerializedName("zubei")
    private Integer footBackArtery;
    @SerializedName("gangmen")
    private Integer anusDre;
    @SerializedName("gangmenQita")
    private String otherAnusDre;
    @SerializedName("ruxian")
    private String mammaryGland;
    @SerializedName("ruxianQita")
    private String otherMammaryGland;
    @SerializedName("waiyin")
    private String vulva;
    @SerializedName("yindao")
    private String vagina;
    @SerializedName("gongjing")
    private String cervical;
    @SerializedName("gongti")
    private String corpus;
    @SerializedName("fukeFujian")
    private String attachment;
    @SerializedName("fukeQita")
    private String otherBodyExam;
    @SerializedName("xuehongdanbai")
    private Double hemoglobin;
    @SerializedName("baixibao")
    private Double whiteBloodCells;
    @SerializedName("xuexiaoban")
    private Double platelet;
    @SerializedName("xuechangguiQita")
    private String otherBlood;
    @SerializedName("niaodanbai")
    private String urineProteim;
    @SerializedName("niaotang")
    private String urineSuger;
    @SerializedName("niaotongti")
    private String urineKetone;
    @SerializedName("niaoqianxue")
    private String urineEry;
    @SerializedName("niaochangguiQita")
    private String otherUrine;
    @SerializedName("kongfuXuetang")
    private Double fastingGlucose;
    //终端没有上传
    private String fgUnits;
    @SerializedName("xindiantu")
    private String electrocardiogram;
    @SerializedName("niaoweiliang")
    private Double urineTraceAlbumin;
    @SerializedName("dabian")
    private Integer defecateOccultBlood;
    @SerializedName("tanghuaXuehong")
    private Double glycatedHemoglobin;
    @SerializedName("yiganKangti")
    private Integer heapSurAntigen;
    @SerializedName("xueqingGubing")
    private Double serumAlt;
    @SerializedName("xueqingGucao")
    private Double serumAspertateAtf;
    @SerializedName("baidanbai")
    private Double albumin;
    @SerializedName("zongDanhongsu")
    private Double totalBilirubin;
    @SerializedName("jieheDanhongsu")
    private Double combiningBilirubin;
    @SerializedName("xuetangJigan")
    private Double serumCreatinine;
    @SerializedName("xueniaoSudan")
    private Double bloodUreaNitrogen;
    @SerializedName("xuejiaNongdu")
    private Double potassiumConcentration;
    @SerializedName("xuenaNongdu")
    private Double sodiumConcentration;
    @SerializedName("zongdanguchun")
    private Double totalCholesterol;
    @SerializedName("ganyouSanzhi")
    private Double triglycerides;
    @SerializedName("diMiduGuchun")
    private Double sldlc;
    @SerializedName("gaoMiduGuchun")
    private Double shdlc;
    @SerializedName("xXianpian")
    private String chestXRay;
    @SerializedName("bChao")
    private String BUltrasound;
    @SerializedName("gongjingTupian")
    private String cervicalSmears;
    @SerializedName("fuzhuQita")
    private String otherAssistExam;
    @SerializedName("qiheZhi")
    private Integer mildPhysique;
    @SerializedName("qixuZhi")
    private Integer faintPhysical;
    @SerializedName("yangxuZhi")
    private Integer maleQuality;
    @SerializedName("yinxuZhi")
    private Integer lunarQuality;
    @SerializedName("danshiZhi")
    private Integer phlegmDamp;
    @SerializedName("shireZhi")
    private Integer dampnessHeat;
    @SerializedName("xueyuZhi")
    private Integer bloodQuality;
    @SerializedName("qiyuZhi")
    private Integer logisticQuality;
    @SerializedName("tebingZhi")
    private Integer graspQuality;
    @SerializedName("naoXueguan")
    private transient String cerebraveDisease;
    @SerializedName("naoXueguanQita")
    private String otherCereDisease;
    @SerializedName("shenzang")
    private transient String renalDisease;
    @SerializedName("shenzangQita")
    private String otherRenalDisease;
    @SerializedName("xinzang")
    private transient String heartDisease;
    @SerializedName("xinzangQita")
    private String otherHeartDisease;
    private transient String vascularDisease;
    @SerializedName("xueguanQita")
    private String otherVascularDisease;
    @SerializedName("yanbuJibing")
    private transient String eyeDisease;
    @SerializedName("yanbuJibingQita")
    private String otherEyeDisease;
    @SerializedName("shenjing")
    private String ond;
    @SerializedName("shenjingQita")
    private String otherSysDisease;
    @SerializedName("jiankangDJ")
    private Integer healthyLevel;
    @SerializedName("jiankangPJ")
    private String healthyAssess;
    @SerializedName("jiankangZhidao")
    private transient String healthyDirect;
    private transient String dangerControl;
    @SerializedName("weixianKongzhiWeight")
    private Double dangerControlWeight;
    @SerializedName("weixianKongzhiYimiao")
    private String dangerControlVaccin;
    @SerializedName("weixianKongzhiOther")
    private String dangerControlOther;
    //体检机构
    @SerializedName("examWhere")
    private String industryName;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    // 住院史
    private transient List<ExamHospitalHis> examHospitalHiss;
    // 家人住院史
    private transient List<ExamFamilyHosHis> examFamilyHosHiss;
    // 接种史
    private transient List<ExamInoculate> examInoculates;
    // 毒物史
    private transient List<ExamPosion> examPosions;
    // 用药史
    private transient List<ExamMedic> examMedics;
    // 非持久化字段
    private String idCard;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getExamNo()
    {
        return examNo;
    }

    public void setExamNo(String examNo)
    {
        this.examNo = examNo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getExamDate()
    {
        return examDate;
    }

    public void setExamDate(Date examDate)
    {
        this.examDate = examDate;
    }

    public String getSymptom()
    {
        return symptom;
    }

    public void setSymptom(String symptom)
    {
        this.symptom = symptom;
    }

    public String getOtherSymptom()
    {
        return otherSymptom;
    }

    public void setOtherSymptom(String otherSymptom)
    {
        this.otherSymptom = otherSymptom;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public Integer getPulseRate()
    {
        return pulseRate;
    }

    public void setPulseRate(Integer pulseRate)
    {
        this.pulseRate = pulseRate;
    }

    public Integer getBreatheRate()
    {
        return breatheRate;
    }

    public void setBreatheRate(Integer breatheRate)
    {
        this.breatheRate = breatheRate;
    }

    public Integer getLBloodPressure()
    {
        return LBloodPressure;
    }

    public void setLBloodPressure(Integer lBloodPressure)
    {
        LBloodPressure = lBloodPressure;
    }

    public Integer getLBloodPressureBpd()
    {
        return LBloodPressureBpd;
    }

    public void setLBloodPressureBpd(Integer lBloodPressureBpd)
    {
        LBloodPressureBpd = lBloodPressureBpd;
    }

    public Integer getRBloodPressure()
    {
        return RBloodPressure;
    }

    public void setRBloodPressure(Integer rBloodPressure)
    {
        RBloodPressure = rBloodPressure;
    }

    public Integer getRBloodPressureBpd()
    {
        return RBloodPressureBpd;
    }

    public void setRBloodPressureBpd(Integer rBloodPressureBpd)
    {
        RBloodPressureBpd = rBloodPressureBpd;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public Integer getWaistline()
    {
        return waistline;
    }

    public void setWaistline(Integer waistline)
    {
        this.waistline = waistline;
    }

    public Double getBmi()
    {
        return bmi;
    }

    public void setBmi(Double bmi)
    {
        this.bmi = bmi;
    }

    public Integer getAgedHealthAssess()
    {
        return agedHealthAssess;
    }

    public void setAgedHealthAssess(Integer agedHealthAssess)
    {
        this.agedHealthAssess = agedHealthAssess;
    }

    public Integer getAgedLifeAssess()
    {
        return agedLifeAssess;
    }

    public void setAgedLifeAssess(Integer agedLifeAssess)
    {
        this.agedLifeAssess = agedLifeAssess;
    }

    public Integer getAgedKonwledgeFunc()
    {
        return agedKonwledgeFunc;
    }

    public void setAgedKonwledgeFunc(Integer agedKonwledgeFunc)
    {
        this.agedKonwledgeFunc = agedKonwledgeFunc;
    }

    public Integer getAgedKfScore()
    {
        return agedKfScore;
    }

    public void setAgedKfScore(Integer agedKfScore)
    {
        this.agedKfScore = agedKfScore;
    }

    public Integer getAgedFeelingStatus()
    {
        return agedFeelingStatus;
    }

    public void setAgedFeelingStatus(Integer agedFeelingStatus)
    {
        this.agedFeelingStatus = agedFeelingStatus;
    }

    public Integer getAgedFsScore()
    {
        return agedFsScore;
    }

    public void setAgedFsScore(Integer agedFsScore)
    {
        this.agedFsScore = agedFsScore;
    }

    public Integer getExerciseRate()
    {
        return exerciseRate;
    }

    public void setExerciseRate(Integer exerciseRate)
    {
        this.exerciseRate = exerciseRate;
    }

    public Integer getEveryExerTime()
    {
        return everyExerTime;
    }

    public void setEveryExerTime(Integer everyExerTime)
    {
        this.everyExerTime = everyExerTime;
    }

    public Double getInsistExerYear()
    {
        return insistExerYear;
    }

    public void setInsistExerYear(Double insistExerYear)
    {
        this.insistExerYear = insistExerYear;
    }

    public String getExerType()
    {
        return exerType;
    }

    public void setExerType(String exerType)
    {
        this.exerType = exerType;
    }

    public String getDietStatus()
    {
        return dietStatus;
    }

    public void setDietStatus(String dietStatus)
    {
        this.dietStatus = dietStatus;
    }

    public Integer getSmokeStatus()
    {
        return smokeStatus;
    }

    public void setSmokeStatus(Integer smokeStatus)
    {
        this.smokeStatus = smokeStatus;
    }

    public Integer getDailySmoke()
    {
        return dailySmoke;
    }

    public void setDailySmoke(Integer dailySmoke)
    {
        this.dailySmoke = dailySmoke;
    }

    public Integer getStartSmokeAge()
    {
        return startSmokeAge;
    }

    public void setStartSmokeAge(Integer startSmokeAge)
    {
        this.startSmokeAge = startSmokeAge;
    }

    public Integer getNotSmokeAge()
    {
        return notSmokeAge;
    }

    public void setNotSmokeAge(Integer notSmokeAge)
    {
        this.notSmokeAge = notSmokeAge;
    }

    public Integer getDrinkWineRate()
    {
        return drinkWineRate;
    }

    public void setDrinkWineRate(Integer drinkWineRate)
    {
        this.drinkWineRate = drinkWineRate;
    }

    public Integer getDailyDrink()
    {
        return dailyDrink;
    }

    public void setDailyDrink(Integer dailyDrink)
    {
        this.dailyDrink = dailyDrink;
    }

    public Integer getIsNotDrink()
    {
        return isNotDrink;
    }

    public void setIsNotDrink(Integer isNotDrink)
    {
        this.isNotDrink = isNotDrink;
    }

    public Integer getNotDrinkAge()
    {
        return notDrinkAge;
    }

    public void setNotDrinkAge(Integer notDrinkAge)
    {
        this.notDrinkAge = notDrinkAge;
    }

    public Integer getStartDrinkAge()
    {
        return startDrinkAge;
    }

    public void setStartDrinkAge(Integer startDrinkAge)
    {
        this.startDrinkAge = startDrinkAge;
    }

    public Integer getRecentYearDrink()
    {
        return recentYearDrink;
    }

    public void setRecentYearDrink(Integer recentYearDrink)
    {
        this.recentYearDrink = recentYearDrink;
    }

    public String getDrinkType()
    {
        return drinkType;
    }

    public void setDrinkType(String drinkType)
    {
        this.drinkType = drinkType;
    }

    public String getOtherDrinkType()
    {
        return otherDrinkType;
    }

    public void setOtherDrinkType(String otherDrinkType)
    {
        this.otherDrinkType = otherDrinkType;
    }

    public String getWorkType()
    {
        return workType;
    }

    public void setWorkType(String workType)
    {
        this.workType = workType;
    }

    public Double getWorkTime()
    {
        return workTime;
    }

    public void setWorkTime(Double workTime)
    {
        this.workTime = workTime;
    }

    public Integer getLips()
    {
        return lips;
    }

    public void setLips(Integer lips)
    {
        this.lips = lips;
    }

    public Integer getTooth()
    {
        return tooth;
    }

    public void setTooth(Integer tooth)
    {
        this.tooth = tooth;
    }

    public Integer getThroad()
    {
        return throad;
    }

    public void setThroad(Integer throad)
    {
        this.throad = throad;
    }

    public Double getLEyesight()
    {
        return LEyesight;
    }

    public void setLEyesight(Double lEyesight)
    {
        LEyesight = lEyesight;
    }

    public Double getREyesight()
    {
        return REyesight;
    }

    public void setREyesight(Double rEyesight)
    {
        REyesight = rEyesight;
    }

    public Double getReLEyesight()
    {
        return reLEyesight;
    }

    public void setReLEyesight(Double reLEyesight)
    {
        this.reLEyesight = reLEyesight;
    }

    public Double getReREyesight()
    {
        return reREyesight;
    }

    public void setReREyesight(Double reREyesight)
    {
        this.reREyesight = reREyesight;
    }

    public Integer getHearing()
    {
        return hearing;
    }

    public void setHearing(Integer hearing)
    {
        this.hearing = hearing;
    }

    public Integer getSportsFunc()
    {
        return sportsFunc;
    }

    public void setSportsFunc(Integer sportsFunc)
    {
        this.sportsFunc = sportsFunc;
    }

    public String getEyeGround()
    {
        return eyeGround;
    }

    public void setEyeGround(String eyeGround)
    {
        this.eyeGround = eyeGround;
    }

    public Integer getSkin()
    {
        return skin;
    }

    public void setSkin(Integer skin)
    {
        this.skin = skin;
    }

    public String getOtherSkin()
    {
        return otherSkin;
    }

    public void setOtherSkin(String otherSkin)
    {
        this.otherSkin = otherSkin;
    }

    public Integer getSclera()
    {
        return sclera;
    }

    public void setSclera(Integer sclera)
    {
        this.sclera = sclera;
    }

    public String getOtherSclera()
    {
        return otherSclera;
    }

    public void setOtherSclera(String otherSclera)
    {
        this.otherSclera = otherSclera;
    }

    public Integer getLymphaden()
    {
        return lymphaden;
    }

    public void setLymphaden(Integer lymphaden)
    {
        this.lymphaden = lymphaden;
    }

    public String getOtherLymphaden()
    {
        return otherLymphaden;
    }

    public void setOtherLymphaden(String otherLymphaden)
    {
        this.otherLymphaden = otherLymphaden;
    }

    public Integer getLungsChest()
    {
        return lungsChest;
    }

    public void setLungsChest(Integer lungsChest)
    {
        this.lungsChest = lungsChest;
    }

    public String getLungsBreVoice()
    {
        return lungsBreVoice;
    }

    public void setLungsBreVoice(String lungsBreVoice)
    {
        this.lungsBreVoice = lungsBreVoice;
    }

    public Integer getLungsRale()
    {
        return lungsRale;
    }

    public void setLungsRale(Integer lungsRale)
    {
        this.lungsRale = lungsRale;
    }

    public String getOtherLungsRale()
    {
        return otherLungsRale;
    }

    public void setOtherLungsRale(String otherLungsRale)
    {
        this.otherLungsRale = otherLungsRale;
    }

    public Integer getHeartRate()
    {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate)
    {
        this.heartRate = heartRate;
    }

    public Integer getHeartRhythm()
    {
        return heartRhythm;
    }

    public void setHeartRhythm(Integer heartRhythm)
    {
        this.heartRhythm = heartRhythm;
    }

    public String getHeartNoise()
    {
        return heartNoise;
    }

    public void setHeartNoise(String heartNoise)
    {
        this.heartNoise = heartNoise;
    }

    public String getAbdPrePain()
    {
        return abdPrePain;
    }

    public void setAbdPrePain(String abdPrePain)
    {
        this.abdPrePain = abdPrePain;
    }

    public String getAbdBagPiece()
    {
        return abdBagPiece;
    }

    public void setAbdBagPiece(String abdBagPiece)
    {
        this.abdBagPiece = abdBagPiece;
    }

    public String getAbdHepatomegaly()
    {
        return abdHepatomegaly;
    }

    public void setAbdHepatomegaly(String abdHepatomegaly)
    {
        this.abdHepatomegaly = abdHepatomegaly;
    }

    public String getAbdSplenomegaly()
    {
        return abdSplenomegaly;
    }

    public void setAbdSplenomegaly(String abdSplenomegaly)
    {
        this.abdSplenomegaly = abdSplenomegaly;
    }

    public String getAbdMovNoise()
    {
        return abdMovNoise;
    }

    public void setAbdMovNoise(String abdMovNoise)
    {
        this.abdMovNoise = abdMovNoise;
    }

    public Integer getLimbsEdema()
    {
        return limbsEdema;
    }

    public void setLimbsEdema(Integer limbsEdema)
    {
        this.limbsEdema = limbsEdema;
    }

    public Integer getFootBackArtery()
    {
        return footBackArtery;
    }

    public void setFootBackArtery(Integer footBackArtery)
    {
        this.footBackArtery = footBackArtery;
    }

    public Integer getAnusDre()
    {
        return anusDre;
    }

    public void setAnusDre(Integer anusDre)
    {
        this.anusDre = anusDre;
    }

    public String getOtherAnusDre()
    {
        return otherAnusDre;
    }

    public void setOtherAnusDre(String otherAnusDre)
    {
        this.otherAnusDre = otherAnusDre;
    }

    public String getMammaryGland()
    {
        return mammaryGland;
    }

    public void setMammaryGland(String mammaryGland)
    {
        this.mammaryGland = mammaryGland;
    }

    public String getOtherMammaryGland()
    {
        return otherMammaryGland;
    }

    public void setOtherMammaryGland(String otherMammaryGland)
    {
        this.otherMammaryGland = otherMammaryGland;
    }

    public String getVulva()
    {
        return vulva;
    }

    public void setVulva(String vulva)
    {
        this.vulva = vulva;
    }

    public String getVagina()
    {
        return vagina;
    }

    public void setVagina(String vagina)
    {
        this.vagina = vagina;
    }

    public String getCervical()
    {
        return cervical;
    }

    public void setCervical(String cervical)
    {
        this.cervical = cervical;
    }

    public String getCorpus()
    {
        return corpus;
    }

    public void setCorpus(String corpus)
    {
        this.corpus = corpus;
    }

    public String getAttachment()
    {
        return attachment;
    }

    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }

    public String getOtherBodyExam()
    {
        return otherBodyExam;
    }

    public void setOtherBodyExam(String otherBodyExam)
    {
        this.otherBodyExam = otherBodyExam;
    }

    public Double getHemoglobin()
    {
        return hemoglobin;
    }

    public void setHemoglobin(Double hemoglobin)
    {
        this.hemoglobin = hemoglobin;
    }

    public Double getWhiteBloodCells()
    {
        return whiteBloodCells;
    }

    public void setWhiteBloodCells(Double whiteBloodCells)
    {
        this.whiteBloodCells = whiteBloodCells;
    }

    public Double getPlatelet()
    {
        return platelet;
    }

    public void setPlatelet(Double platelet)
    {
        this.platelet = platelet;
    }

    public String getOtherBlood()
    {
        return otherBlood;
    }

    public void setOtherBlood(String otherBlood)
    {
        this.otherBlood = otherBlood;
    }

    public String getUrineProteim()
    {
        return urineProteim;
    }

    public void setUrineProteim(String urineProteim)
    {
        this.urineProteim = urineProteim;
    }

    public String getUrineSuger()
    {
        return urineSuger;
    }

    public void setUrineSuger(String urineSuger)
    {
        this.urineSuger = urineSuger;
    }

    public String getUrineKetone()
    {
        return urineKetone;
    }

    public void setUrineKetone(String urineKetone)
    {
        this.urineKetone = urineKetone;
    }

    public String getUrineEry()
    {
        return urineEry;
    }

    public void setUrineEry(String urineEry)
    {
        this.urineEry = urineEry;
    }

    public String getOtherUrine()
    {
        return otherUrine;
    }

    public void setOtherUrine(String otherUrine)
    {
        this.otherUrine = otherUrine;
    }

    public Double getFastingGlucose()
    {
        return fastingGlucose;
    }

    public void setFastingGlucose(Double fastingGlucose)
    {
        this.fastingGlucose = fastingGlucose;
    }

    public String getFgUnits()
    {
        return fgUnits;
    }

    public void setFgUnits(String fgUnits)
    {
        this.fgUnits = fgUnits;
    }

    public String getElectrocardiogram()
    {
        return electrocardiogram;
    }

    public void setElectrocardiogram(String electrocardiogram)
    {
        this.electrocardiogram = electrocardiogram;
    }

    public Double getUrineTraceAlbumin()
    {
        return urineTraceAlbumin;
    }

    public void setUrineTraceAlbumin(Double urineTraceAlbumin)
    {
        this.urineTraceAlbumin = urineTraceAlbumin;
    }

    public Integer getDefecateOccultBlood()
    {
        return defecateOccultBlood;
    }

    public void setDefecateOccultBlood(Integer defecateOccultBlood)
    {
        this.defecateOccultBlood = defecateOccultBlood;
    }

    public Double getGlycatedHemoglobin()
    {
        return glycatedHemoglobin;
    }

    public void setGlycatedHemoglobin(Double glycatedHemoglobin)
    {
        this.glycatedHemoglobin = glycatedHemoglobin;
    }

    public Integer getHeapSurAntigen()
    {
        return heapSurAntigen;
    }

    public void setHeapSurAntigen(Integer heapSurAntigen)
    {
        this.heapSurAntigen = heapSurAntigen;
    }

    public Double getSerumAlt()
    {
        return serumAlt;
    }

    public void setSerumAlt(Double serumAlt)
    {
        this.serumAlt = serumAlt;
    }

    public Double getSerumAspertateAtf()
    {
        return serumAspertateAtf;
    }

    public void setSerumAspertateAtf(Double serumAspertateAtf)
    {
        this.serumAspertateAtf = serumAspertateAtf;
    }

    public Double getAlbumin()
    {
        return albumin;
    }

    public void setAlbumin(Double albumin)
    {
        this.albumin = albumin;
    }

    public Double getTotalBilirubin()
    {
        return totalBilirubin;
    }

    public void setTotalBilirubin(Double totalBilirubin)
    {
        this.totalBilirubin = totalBilirubin;
    }

    public Double getCombiningBilirubin()
    {
        return combiningBilirubin;
    }

    public void setCombiningBilirubin(Double combiningBilirubin)
    {
        this.combiningBilirubin = combiningBilirubin;
    }

    public Double getSerumCreatinine()
    {
        return serumCreatinine;
    }

    public void setSerumCreatinine(Double serumCreatinine)
    {
        this.serumCreatinine = serumCreatinine;
    }

    public Double getBloodUreaNitrogen()
    {
        return bloodUreaNitrogen;
    }

    public void setBloodUreaNitrogen(Double bloodUreaNitrogen)
    {
        this.bloodUreaNitrogen = bloodUreaNitrogen;
    }

    public Double getPotassiumConcentration()
    {
        return potassiumConcentration;
    }

    public void setPotassiumConcentration(Double potassiumConcentration)
    {
        this.potassiumConcentration = potassiumConcentration;
    }

    public Double getSodiumConcentration()
    {
        return sodiumConcentration;
    }

    public void setSodiumConcentration(Double sodiumConcentration)
    {
        this.sodiumConcentration = sodiumConcentration;
    }

    public Double getTotalCholesterol()
    {
        return totalCholesterol;
    }

    public void setTotalCholesterol(Double totalCholesterol)
    {
        this.totalCholesterol = totalCholesterol;
    }

    public Double getTriglycerides()
    {
        return triglycerides;
    }

    public void setTriglycerides(Double triglycerides)
    {
        this.triglycerides = triglycerides;
    }

    public Double getSldlc()
    {
        return sldlc;
    }

    public void setSldlc(Double sldlc)
    {
        this.sldlc = sldlc;
    }

    public Double getShdlc()
    {
        return shdlc;
    }

    public void setShdlc(Double shdlc)
    {
        this.shdlc = shdlc;
    }

    public String getChestXRay()
    {
        return chestXRay;
    }

    public void setChestXRay(String chestXRay)
    {
        this.chestXRay = chestXRay;
    }

    public String getBUltrasound()
    {
        return BUltrasound;
    }

    public void setBUltrasound(String bUltrasound)
    {
        BUltrasound = bUltrasound;
    }

    public String getCervicalSmears()
    {
        return cervicalSmears;
    }

    public void setCervicalSmears(String cervicalSmears)
    {
        this.cervicalSmears = cervicalSmears;
    }

    public String getOtherAssistExam()
    {
        return otherAssistExam;
    }

    public void setOtherAssistExam(String otherAssistExam)
    {
        this.otherAssistExam = otherAssistExam;
    }

    public Integer getMildPhysique()
    {
        return mildPhysique;
    }

    public void setMildPhysique(Integer mildPhysique)
    {
        this.mildPhysique = mildPhysique;
    }

    public Integer getFaintPhysical()
    {
        return faintPhysical;
    }

    public void setFaintPhysical(Integer faintPhysical)
    {
        this.faintPhysical = faintPhysical;
    }

    public Integer getMaleQuality()
    {
        return maleQuality;
    }

    public void setMaleQuality(Integer maleQuality)
    {
        this.maleQuality = maleQuality;
    }

    public Integer getLunarQuality()
    {
        return lunarQuality;
    }

    public void setLunarQuality(Integer lunarQuality)
    {
        this.lunarQuality = lunarQuality;
    }

    public Integer getPhlegmDamp()
    {
        return phlegmDamp;
    }

    public void setPhlegmDamp(Integer phlegmDamp)
    {
        this.phlegmDamp = phlegmDamp;
    }

    public Integer getDampnessHeat()
    {
        return dampnessHeat;
    }

    public void setDampnessHeat(Integer dampnessHeat)
    {
        this.dampnessHeat = dampnessHeat;
    }

    public Integer getBloodQuality()
    {
        return bloodQuality;
    }

    public void setBloodQuality(Integer bloodQuality)
    {
        this.bloodQuality = bloodQuality;
    }

    public Integer getLogisticQuality()
    {
        return logisticQuality;
    }

    public void setLogisticQuality(Integer logisticQuality)
    {
        this.logisticQuality = logisticQuality;
    }

    public Integer getGraspQuality()
    {
        return graspQuality;
    }

    public void setGraspQuality(Integer graspQuality)
    {
        this.graspQuality = graspQuality;
    }

    public String getCerebraveDisease()
    {
        return cerebraveDisease;
    }

    public void setCerebraveDisease(String cerebraveDisease)
    {
        this.cerebraveDisease = cerebraveDisease;
    }

    public String getOtherCereDisease()
    {
        return otherCereDisease;
    }

    public void setOtherCereDisease(String otherCereDisease)
    {
        this.otherCereDisease = otherCereDisease;
    }

    public String getRenalDisease()
    {
        return renalDisease;
    }

    public void setRenalDisease(String renalDisease)
    {
        this.renalDisease = renalDisease;
    }

    public String getOtherRenalDisease()
    {
        return otherRenalDisease;
    }

    public void setOtherRenalDisease(String otherRenalDisease)
    {
        this.otherRenalDisease = otherRenalDisease;
    }

    public String getHeartDisease()
    {
        return heartDisease;
    }

    public void setHeartDisease(String heartDisease)
    {
        this.heartDisease = heartDisease;
    }

    public String getOtherHeartDisease()
    {
        return otherHeartDisease;
    }

    public void setOtherHeartDisease(String otherHeartDisease)
    {
        this.otherHeartDisease = otherHeartDisease;
    }

    public String getVascularDisease()
    {
        return vascularDisease;
    }

    public void setVascularDisease(String vascularDisease)
    {
        this.vascularDisease = vascularDisease;
    }

    public String getOtherVascularDisease()
    {
        return otherVascularDisease;
    }

    public void setOtherVascularDisease(String otherVascularDisease)
    {
        this.otherVascularDisease = otherVascularDisease;
    }

    public String getEyeDisease()
    {
        return eyeDisease;
    }

    public void setEyeDisease(String eyeDisease)
    {
        this.eyeDisease = eyeDisease;
    }

    public String getOtherEyeDisease()
    {
        return otherEyeDisease;
    }

    public void setOtherEyeDisease(String otherEyeDisease)
    {
        this.otherEyeDisease = otherEyeDisease;
    }

    public String getOnd()
    {
        return ond;
    }

    public void setOnd(String ond)
    {
        this.ond = ond;
    }

    public String getOtherSysDisease()
    {
        return otherSysDisease;
    }

    public void setOtherSysDisease(String otherSysDisease)
    {
        this.otherSysDisease = otherSysDisease;
    }

    public Integer getHealthyLevel()
    {
        return healthyLevel;
    }

    public void setHealthyLevel(Integer healthyLevel)
    {
        this.healthyLevel = healthyLevel;
    }

    public String getHealthyAssess()
    {
        return healthyAssess;
    }

    public void setHealthyAssess(String healthyAssess)
    {
        this.healthyAssess = healthyAssess;
    }

    public String getHealthyDirect()
    {
        return healthyDirect;
    }

    public void setHealthyDirect(String healthyDirect)
    {
        this.healthyDirect = healthyDirect;
    }

    public String getDangerControl()
    {
        return dangerControl;
    }

    public void setDangerControl(String dangerControl)
    {
        this.dangerControl = dangerControl;
    }

    public Double getDangerControlWeight()
    {
        return dangerControlWeight;
    }

    public void setDangerControlWeight(Double dangerControlWeight)
    {
        this.dangerControlWeight = dangerControlWeight;
    }

    public String getDangerControlVaccin()
    {
        return dangerControlVaccin;
    }

    public void setDangerControlVaccin(String dangerControlVaccin)
    {
        this.dangerControlVaccin = dangerControlVaccin;
    }

    public String getDangerControlOther()
    {
        return dangerControlOther;
    }

    public void setDangerControlOther(String dangerControlOther)
    {
        this.dangerControlOther = dangerControlOther;
    }

    public String getIndustryName()
    {
        return industryName;
    }

    public void setIndustryName(String industryName)
    {
        this.industryName = industryName;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public List<ExamHospitalHis> getExamHospitalHiss()
    {
        return examHospitalHiss;
    }

    public void setExamHospitalHiss(List<ExamHospitalHis> examHospitalHiss)
    {
        this.examHospitalHiss = examHospitalHiss;
    }

    public List<ExamFamilyHosHis> getExamFamilyHosHiss()
    {
        return examFamilyHosHiss;
    }

    public void setExamFamilyHosHiss(List<ExamFamilyHosHis> examFamilyHosHiss)
    {
        this.examFamilyHosHiss = examFamilyHosHiss;
    }

    public List<ExamInoculate> getExamInoculates()
    {
        return examInoculates;
    }

    public void setExamInoculates(List<ExamInoculate> examInoculates)
    {
        this.examInoculates = examInoculates;
    }

    public List<ExamPosion> getExamPosions()
    {
        return examPosions;
    }

    public void setExamPosions(List<ExamPosion> examPosions)
    {
        this.examPosions = examPosions;
    }

    public List<ExamMedic> getExamMedics()
    {
        return examMedics;
    }

    public void setExamMedics(List<ExamMedic> examMedics)
    {
        this.examMedics = examMedics;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

}