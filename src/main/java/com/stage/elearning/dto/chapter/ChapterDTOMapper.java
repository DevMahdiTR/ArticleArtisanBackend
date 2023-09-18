package com.stage.elearning.dto.chapter;

import com.stage.elearning.model.chapter.Chapter;

import java.util.function.Function;

public class ChapterDTOMapper implements Function<Chapter, ChapterDTO> {
    @Override
    public ChapterDTO apply(Chapter chapter) {
        return new ChapterDTO(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getDescription()
        );
    }
}
