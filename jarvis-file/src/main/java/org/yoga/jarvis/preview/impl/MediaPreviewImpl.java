/*
 *  Copyright 2022 yoga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.yoga.jarvis.preview.impl;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.constant.DelimiterType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Description: media preview
 * @Author: yoga
 * @Date: 2023/8/4 17:34
 */
@Slf4j
public class MediaPreviewImpl extends AbstractPreview {

    /**
     * need convert suffix list
     */
    public static final List<String> NEED_CONVERT_SUFFIX_LIST = Arrays.asList("avi", "mov", "wmv", "mkv");

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        File previewTmpFile = new File(destDir.getPath() + File.separator + UUID.randomUUID() +
                DelimiterType.point.getValue() + "mp4");

        // convert src file
        if (checkIsNeedConvert(srcFile.getName())) {
            try (FrameGrabber fg = new FFmpegFrameGrabber(srcFile)) {
                fg.start();
                try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(previewTmpFile.getPath() + File.separator + previewTmpFile.getName(),
                        fg.getImageWidth(), fg.getImageHeight(), fg.getAudioChannels())) {
                    recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                    recorder.setFormat("mp4");
                    recorder.setFrameRate(fg.getFrameRate());
                    recorder.setSampleRate(fg.getSampleRate());
                    recorder.setVideoBitrate(fg.getVideoBitrate());
                    recorder.setAspectRatio(fg.getAspectRatio());
                    recorder.setAudioBitrate(fg.getAudioBitrate());
                    recorder.setAudioOptions(fg.getAudioOptions());
                    recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
                    recorder.setAudioChannels(fg.getAudioChannels());
                    recorder.start();
                    while (true) {
                        try (Frame capturedFrame = fg.grabFrame()) {
                            if (capturedFrame == null) {
                                log.info("transcoding completed!");
                                break;
                            }
                            recorder.setTimestamp(fg.getTimestamp());
                            recorder.record(capturedFrame);
                        }
                    }
                    recorder.stop();
                    recorder.release();
                } catch (FrameRecorder.Exception e) {
                    throw new JarvisException(e);
                } finally {
                    fg.stop();
                }
            } catch (FrameGrabber.Exception e) {
                throw new JarvisException(e);
            }
        }
        return previewTmpFile;
    }

    /**
     * check file is need convert
     *
     * @param fileName file name
     * @return is need convert
     */
    private boolean checkIsNeedConvert(String fileName) {
        Assert.notBlank(fileName, "file name is blank!");
        return NEED_CONVERT_SUFFIX_LIST.contains(FileUtils.getFileSuffix(fileName));
    }
}
