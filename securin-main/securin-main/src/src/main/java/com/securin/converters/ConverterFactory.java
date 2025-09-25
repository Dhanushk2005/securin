package com.securin.converters;
/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
public final class ConverterFactory{

    /**
     * You should implement this method having it return your version of
     * {@link com.securin.converters.XMLJSONConverterI}.
     *
     * @return {@link com.securin.converters.XMLJSONConverterI} implementation you created.
     */
    public static final XMLJSONConverterI createXMLJSONConverter() {
       // Todo: Implement this method please.
        return new xml_converter();
    }

}
