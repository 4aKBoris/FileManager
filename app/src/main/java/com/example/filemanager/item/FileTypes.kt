package com.example.filemanager.item

import androidx.annotation.DrawableRes
import com.example.filemanager.R
import com.example.filemanager.constants.Folder
import java.io.File

enum class FileTypes(@DrawableRes val idRes: Int, val type: String) {
    FILE(idRes = R.drawable.file, type = ""),
    Z7(idRes = R.drawable.z7, type = "7z"),
    AVI(idRes = R.drawable.avi, type = "avi"),
    DOC(idRes = R.drawable.doc, type = "doc"),
    DOCX(idRes = R.drawable.doc, type = "docx"),
    XML(idRes = R.drawable.xml, type = "xml"),
    HTML(idRes = R.drawable.html, type = "html"),
    TIFF(idRes = R.drawable.tiff, type = "tiff"),
    EXE(idRes = R.drawable.exe, type = "exe"),
    GIF(idRes = R.drawable.gif, type = "gif"),
    JPG(idRes = R.drawable.jpg, type = "jpg"),
    JS(idRes = R.drawable.js, type = "js"),
    MP2(idRes = R.drawable.mp2, type = "mp2"),
    MP3(idRes = R.drawable.mp3, type = "mp3"),
    MP4(idRes = R.drawable.mp4, type = "mp4"),
    PDF(idRes = R.drawable.pdf, type = "pdf"),
    CSS(idRes = R.drawable.css, type = "css"),
    PNG(idRes = R.drawable.png, type = "png"),
    PPT(idRes = R.drawable.ppt, type = "ppt"),
    PPTX(idRes = R.drawable.ppt, type = "pptx"),
    RAR(idRes = R.drawable.rar, type ="rar"),
    TXT(idRes = R.drawable.txt, type = "txt"),
    WAV(idRes = R.drawable.wav, type = "wav"),
    CSV(idRes = R.drawable.csv, type = "csv"),
    CSVX(idRes = R.drawable.csv, type = "csvx"),
    XLS(idRes = R.drawable.xls, type = "xls"),
    XLSX(idRes = R.drawable.xls, type = "xlsx"),
    JSON(idRes = R.drawable.json, type = "json"),
    ZIP(idRes = R.drawable.zip, type = "zip"),
    ISO(idRes = R.drawable.iso, type = "iso"),
    PSD(idRes = R.drawable.psd, type = "psd"),
    SVG(idRes = R.drawable.svg, type = "svg"),
    FLAC(idRes = R.drawable.flac, type = "flac"),
    RTF(idRes = R.drawable.rtf, type = "rtf"),
    AAC(idRes = R.drawable.aac, type = "aac"),
    FLA(idRes = R.drawable.fla, type = "fla"),
    WMA(idRes = R.drawable.wma, type = "wma"),
    MDF(idRes = R.drawable.mdf, type = "mdf"),
    FOLDER(idRes = R.drawable.ic_folder, type = Folder);

}

fun findItem(name: String) = FileTypes.values().find { it.type == name } ?: FileTypes.FILE

fun getType(file: File) = if (file.isDirectory) FileTypes.FOLDER else findItem(file.extension)
