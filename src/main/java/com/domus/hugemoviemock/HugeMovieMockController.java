package com.domus.hugemoviemock;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("mock-movie")
@Slf4j
public class HugeMovieMockController {
    @Value("${mock.movie.total}")
    private int totalMovies;

    @Value("${mock.movie.pagesize}")
    private int pageSize;

    @Value("${mock.movie.director_repetition}")
    private String directorRepetition;

    private Map<Integer, List<String>> repeatedDirectorsPerPageMap = new LinkedHashMap<>();

    private int totalPages;

    @PostConstruct
    private void postConstruct() {
        totalPages = totalMovies / pageSize + (totalMovies % pageSize == 0 ? 0 : 1);
        randomizeRepeatedDirectors();
    }

    private void randomizeRepeatedDirectors() {
        List<String> allRepatedDirectorNames = new ArrayList<>();
        Map<Integer, Integer> repeatedMoviesByDirectorCount = new LinkedHashMap<>();
        List<String> repeatList = List.of(directorRepetition.split(","));
        for (String repeatElt : repeatList) {
            List<String> repeatPieces = List.of(repeatElt.split("/"));
            int nofMovies = Integer.parseInt(repeatPieces.get(0));
            int directorCount = Integer.parseInt(repeatPieces.get(1));
            repeatedMoviesByDirectorCount.put(directorCount, nofMovies);
            for (int d = 1; d <= directorCount; d++) {
                String directorName = String.format("Director with %04d movies, %04d of %04d",
                        nofMovies, d, directorCount);
                for (int times=0; times < nofMovies; times++) {
                    allRepatedDirectorNames.add(directorName);
                }
                int k=0;
            }
            int y=0;
        }
        Collections.shuffle(allRepatedDirectorNames);

        int d = 0;
        int totalRepeatedDirectors = allRepatedDirectorNames.size();
        int repeatedPerPage = totalRepeatedDirectors / totalPages + (totalRepeatedDirectors % totalPages == 0 ? 0 : 1);
        for (int p = 1; p <= totalPages && d < totalRepeatedDirectors; p++) {
            var directorsInPage = new ArrayList<String>();
            for (int z = 0; z < repeatedPerPage; z++) {
                String directorName = allRepatedDirectorNames.get(d++);
                directorsInPage.add(directorName);
                if (d == totalRepeatedDirectors) {
                    break;
                }
            }
            repeatedDirectorsPerPageMap.put(p, directorsInPage);
        }

        int z=0;


}

    private final Map<UUID, Integer> retriesAfterSentMap = new LinkedHashMap<>();

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageDTO> getPageInfo(@RequestParam int page) {
        // TODO - randomize directors , no real data, like "Director 0012 of 1023" is good enogh
        // TOO - all other data can be bogus, like uuids
        if (page < 1 || page > totalPages) {
            throw new IllegalArgumentException("page parameter must be from 1 to " + totalPages);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPerPage(pageSize);
        pageDTO.setPage(page);
        pageDTO.setTotalPages(totalPages);
        pageDTO.setTotal(totalMovies);
        List<MovieDTO> movieDTOList = new ArrayList<>();
        List<String> repeatedDirectorsOfPage = Optional.ofNullable(repeatedDirectorsPerPageMap.get(page))
                .orElse(new ArrayList<>());

        for (int i = 1; i <= pageSize; i++) {
            String directorName;
            if (i <= repeatedDirectorsOfPage.size()) {
                directorName = repeatedDirectorsOfPage.get(i-1);
            } else {
                directorName = "the director " + "(page="+page+")" + i;
            }

            MovieDTO movieDTO = MovieDTO.builder()
                    .title("the title " + "(page="+page+")" + i)
                    .year("the year " + "(page="+page+")" + i)
                    .rated("the rated " + "(page="+page+")" + i)
                    .released("the released " + "(page="+page+")" + i)
                    .runtime("the runtime " + "(page="+page+")" + i)
                    .genre("the genre " + "(page="+page+")" + i)
                    .director(directorName)
                    .writer("the writer " + "(page="+page+")" + i)
                    .actors("the actors " + "(page="+page+")" + i)
                    .build();
            movieDTOList.add(movieDTO);
        }
        pageDTO.setData(movieDTOList);

        return ResponseEntity.ok(pageDTO);
    }




}


