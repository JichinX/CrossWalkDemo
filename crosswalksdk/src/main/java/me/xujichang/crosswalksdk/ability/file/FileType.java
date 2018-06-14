package me.xujichang.crosswalksdk.ability.file;

/**
 * Des:文件类型
 *
 * @author xujichang
 * created at 2018/6/12 - 10:25
 */
public enum FileType {
    /*图片*/IMAGE,
    /*视频*/VIDEO,
    /*音频*/AUDIO,
    /*文本*/TXT,
    /*未匹配*/TYPE_NONE;

    public static FileType obtainType(String type) {
        return obtainType(type, null);
    }

    public static FileType obtainType(String type, String mimeType) {
        if (type == null) {
            throw new RuntimeException("No type value from type: null");
        }
        String low = type.toLowerCase();
        FileType fileType = null;
        switch (low) {
            case "image":
                fileType = IMAGE;
                break;
            case "video":
                fileType = VIDEO;
                break;
            case "audio":
                fileType = AUDIO;
                break;
            case "txt":
                fileType = TXT;
                break;
            default:
                fileType = TYPE_NONE;
                break;
        }
        return fileType;
    }
}
