package ru.practicum.compilation.model;

import lombok.*;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;
import ru.practicum.event.model.Event;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 300, nullable = false)
    private String title;
    @Column(nullable = false)
    private Boolean pinned;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ToString.Exclude
    private Set<Event> events = new HashSet<>();
}