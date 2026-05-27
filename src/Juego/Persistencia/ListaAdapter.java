package Juego.Persistencia;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import Structures.Lista;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador Gson para serializar/deserializar ListaEnlazada como array JSON.
 *
 * Gson espera java.util.List por defecto. Este adaptador traduce entre
 * ListaEnlazada (propia) y el formato de array JSON estándar.
 */
public class ListaAdapter implements JsonSerializer<Lista<?>>,
        JsonDeserializer<Lista<?>> {

    @Override
    public JsonElement serialize(Lista<?> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (int i = 0; i < src.size(); i++) {
            Object elemento = src.get(i);
            array.add(context.serialize(elemento));
        }
        return array;
    }

    @Override
    public Lista<?> deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context)
            throws JsonParseException {

        // Obtener el tipo del elemento genérico (lo que hay dentro de ListaEnlazada<...>)
        Type tipoElemento;
        if (typeOfT instanceof ParameterizedType) {
            tipoElemento = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
        } else {
            tipoElemento = Object.class;
        }

        Lista<Object> lista = new Lista<>();
        JsonArray array = json.getAsJsonArray();

        for (JsonElement elemento : array) {
            Object valor = context.deserialize(elemento, tipoElemento);
            lista.add(valor);
        }

        return lista;
    }
}