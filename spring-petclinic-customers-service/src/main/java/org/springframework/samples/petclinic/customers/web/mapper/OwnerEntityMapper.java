package org.springframework.samples.petclinic.customers.web.mapper;

import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.web.OwnerRequest;
import org.springframework.stereotype.Component;

@Component
public class OwnerEntityMapper implements Mapper<OwnerRequest, Owner> {
    // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
    @Override
    public Owner map(final Owner owner, final OwnerRequest request) {
        owner.setAddress(request.address());
        owner.setName(request.firstName());
        owner.setName(request.lastName());
        return owner;
    }
}
