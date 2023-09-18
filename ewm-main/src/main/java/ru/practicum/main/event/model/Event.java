package ru.practicum.main.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.location.model.Location;
import ru.practicum.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "annotation", nullable = false, length = 2000)
    String annotation;

    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;

    @Column(name = "description", nullable = false, length = 7000)
    String description;

    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    @Column(name = "paid", nullable = false)
    Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    Integer participantLimit;

    @Column(name = "request_moderation", nullable = false)
    Boolean requestModeration;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "title", nullable = false, length = 120)
    String title;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    EventState state;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    Location location;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    User initiator;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @Version
    Long version;

}
