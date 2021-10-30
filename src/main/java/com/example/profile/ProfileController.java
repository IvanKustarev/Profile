package com.example.profile;

import com.example.profile.models.Books;
import com.example.profile.repositories.BooksRepository;
import com.example.profile.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/{id}/")
    public String profileGet(@PathVariable("id") Long id, Model model) {
        List<Books> booksList = booksUsedBy(id);
        model.addAttribute("booksList", booksList);
        return "profile";
    }

    @PostMapping("/{id}/returnBook")
    public String returnBook(@PathVariable("id") Long userId, @RequestParam("bookId") Long bookId) {
        try {
            Books book = booksRepository.findById(bookId).get();
            book.setUsedBy(null);
            booksRepository.save(book);
        } catch (Exception e) {
        }
        return "redirect:/profile/" + userId + "/";
    }

    public List<Books> booksUsedBy(Long id) {
        Iterator<Books> booksIterator = booksRepository.findAll().iterator();
        List<Books> booksList = new ArrayList<>();
        while (booksIterator.hasNext()) {
            Books book = booksIterator.next();
            if (book.getUsedBy() != null) {
                if (book.getUsedBy().equals(id)) {
                    booksList.add(book);
                }
            }
        }
        return booksList;
    }
}
