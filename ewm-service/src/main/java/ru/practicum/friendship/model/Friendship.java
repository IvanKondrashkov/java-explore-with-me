package ru.practicum.friendship.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import ru.practicum.user.model.User;

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
    @Column(name = "is_friend", nullable = false)
    private Boolean isFriend;
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