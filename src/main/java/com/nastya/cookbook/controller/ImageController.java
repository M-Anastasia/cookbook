package com.nastya.cookbook.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fishn on 11.09.2019.
 */
@Controller
@RequestMapping(value = "/uploads")
public class ImageController {

    @RequestMapping(value = "/getimage.html", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@RequestParam(value="fileName") String  fileName) throws IOException{
        String path = System.getenv("RECIPES_IMAGES");
        InputStream in = new FileInputStream(path+fileName);

        return IOUtils.toByteArray(in);
    }

}
