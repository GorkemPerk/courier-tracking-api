package org.migros.one.couriertrackingapi.presentation.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.migros.one.couriertrackingapi.presentation.model.error.ErrorResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private T data;
    private ErrorResponse error;

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        return response;
    }

    public static <T> Response<T> error(ErrorResponse error) {
        Response<T> response = new Response<>();
        response.setError(error);
        return response;
    }
}
