package ru.practicum.event.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import ru.practicum.user.model.User;
import ru.practicum.category.model.Category;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 120, nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String annotation;
    @Column(length = 7000, nullable = false)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(nullable = false)
    private Boolean paid;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;
    @Transient
    private Integer views;
    @Column(length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    private User initiator;
}