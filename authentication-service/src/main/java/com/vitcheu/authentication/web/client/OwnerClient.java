
package com.vitcheu.authentication.web.client;

import com.vitcheu.common.model.request.AddUserRequest;

public interface OwnerClient {
    /**
     *   
     * @return true indicates that the user has been added successfully,
     * otherwise false
     */
    public boolean addUser(AddUserRequest request);
}
