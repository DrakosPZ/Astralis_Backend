package com.Astralis.backend.model;

import com.Astralis.backend.dto.OccasionTagDto;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OccasionTag extends AbstractModel {

    private String tag;
    private String colourHEX;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Occasion> presentIn  = new ArrayList<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Person belongsTo;

    public OccasionTag(OccasionTagDto occasionTagDto){
        this.identifier = Optional.ofNullable(occasionTagDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.tag = occasionTagDto.getTag() == null ? "" : occasionTagDto.getTag();
        this.colourHEX = occasionTagDto.getColourHEX() == null ? "" : occasionTagDto.getColourHEX();
    }








    //----------------------N:M Relationship Methods----------------------
    public void addOccasion(Occasion occasion) {
        if (presentIn.contains(occasion)) {
            return;
        }
        presentIn.add(occasion);
        if (this.getId() != null) {
            occasion.addOccasionTag(this);
        }
    }

    public void removeOccasion(Occasion occasion) {
        if (!presentIn.contains(occasion)) {
            return;
        }
        presentIn.remove(occasion);
        occasion.removeOccasionTag(this);
    }





    //----------------------N:1 Relationship Methods----------------------
    public void setBelongsTo(Person person) {
        if (Objects.equals(this.belongsTo, person)) {
            return;
        }

        Person oldPerson = this.belongsTo;
        this.belongsTo = person;

        if (oldPerson != null) {
            oldPerson.removeOccasionTag(this);
        }

        if (person != null) {
            person.addOccasionTag(this);
        }
    }





    //----------------------Custome Helper Methods----------------------
    public int getPresentInIndex(String identifier){
        for (int i = 0; i < presentIn.size(); i++){
            if(presentIn.get(i).getIdentifier().equals(identifier)){
                return i;
            }
        }
        return -1;
    }

    public void replaceInPresentIn(Occasion occasion){
        presentIn.set(getPresentInIndex(occasion.getIdentifier()),occasion);
    }
}
