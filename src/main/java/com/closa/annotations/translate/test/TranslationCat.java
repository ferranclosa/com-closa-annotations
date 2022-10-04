package com.closa.annotations.translate.test;

import com.closa.annotations.translate.interfaces.Translate;
import com.closa.annotations.translate.interfaces.Translation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Translation(resourceBundle = "translatetext")
public class TranslationCat {

    @Translate(key = "name")
    private String name;

    @Translate(key = "favourite")
    private String treasure;

    @Translate(key="uk")
    private String sayUK;

    //@Translate(key="invalid")
    private Integer age;

    @Translate(key="balaclava")
    private String balaclava;

    public TranslationCat(String name, String treasure, String sayUK, Integer age) {
        this.name = name;
        this.treasure = treasure;
        this.sayUK = sayUK;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTreasure() {
        return treasure;
    }

    public void setTreasure(String treasure) {
        this.treasure = treasure;
    }

    public String getSayUK() {
        return sayUK;
    }

    public void setSayUK(String sayUK) {
        this.sayUK = sayUK;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBalaclava() {
        return balaclava;
    }

    public void setBalaclava(String balaclava) {
        this.balaclava = balaclava;
    }

    public String talk(){
        return ("Mioew!");
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", treasure='" + treasure + '\'' +
                ", age=" + age +
                '}';
    }
}
