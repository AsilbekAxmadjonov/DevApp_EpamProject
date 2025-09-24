package org.example.capstoneproject.mapper;

import lombok.Data;
import org.example.capstoneproject.dto.PostDto;
import org.springframework.stereotype.Component;
import org.example.capstoneproject.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class PostMapper {

    public static List<PostDto> toDtoList(List<Post> posts){
        List<PostDto> list = new ArrayList<>();
        for(var post : posts){
            PostDto postDto = new PostDto();

            postDto.setId(post.getId());
            postDto.setContent(post.getContent());

            list.add(postDto);
        }

        return list;
    }
}
