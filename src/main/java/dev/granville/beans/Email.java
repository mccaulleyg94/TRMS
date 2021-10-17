package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email")
@Getter
@Setter
public class Email implements Comparable<Email> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Integer emailId;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Employee Recipient;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Employee Sender;
    private String context;
    private String title;
    private LocalDateTime sentTime;
    @Override
    public int compareTo(@NotNull Email o) {
        return this.sentTime.compareTo(o.sentTime);
    }
}
