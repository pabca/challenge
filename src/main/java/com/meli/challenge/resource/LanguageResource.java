package com.meli.challenge.resource;

import com.meli.challenge.model.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResource {

    private String isoCode;
    private String name;
    private String nativeName;

    public LanguageResource(Language languageModel) {
        this.isoCode = languageModel.getIso639_1();
        this.name = languageModel.getName();
        this.nativeName = languageModel.getNativeName();
    }
}
