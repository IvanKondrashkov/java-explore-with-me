package ru.practicum.request.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import ru.practicum.user.model.User;
import ru.practicum.event.model.Event;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime created;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @ToString.Exclude
    private User requester;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;
}