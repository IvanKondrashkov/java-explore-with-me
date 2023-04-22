package ru.practicum.friendship.model;

import lombok.*;
import javax.persistence.*;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Boolean status;
    @Column(nullable = false)
    private LocalDateTime created;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    private User initiator;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    @ToString.Exclude
    private User friend;
}