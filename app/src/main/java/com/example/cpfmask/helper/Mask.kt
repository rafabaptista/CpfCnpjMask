package com.example.cpfmask.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Mask {
    companion object {
        private const val NUMBER_CHAR_DOCUMENT_CPF = 11
        private const val NUMBER_CHAR_DOCUMENT_CNPJ = 14
        const val MASK_CPF = "###.###.###-##"
        const val MASK_CNPJ = "##.###.###/####-##"

        private fun replaceChars(cpfFull : String) : String{
            return cpfFull.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }


        fun mask(etCpf : EditText) : TextWatcher {
            val textWatcher : TextWatcher = object : TextWatcher {
                var isUpdating : Boolean = false
                var oldString : String = ""
                var oldStringWithMask : String = ""
                var isDeletingDot = false
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                    if(charSequence.length <= MASK_CNPJ.length) {

                        val typedCharSequence = replaceChars(charSequence.toString())
                        val mask: String
                        val defaultMask = getDefaultMask(typedCharSequence)
                        mask = when (typedCharSequence.length) {
                            NUMBER_CHAR_DOCUMENT_CPF -> MASK_CPF
                            NUMBER_CHAR_DOCUMENT_CNPJ -> MASK_CNPJ
                            else -> defaultMask
                        }

                        if (start != 0 && count == 0 && (oldStringWithMask[start] == '.' || oldStringWithMask[start] == '/' || oldStringWithMask[start] == '-')) {
                            isDeletingDot = true
                            etCpf.setText(oldStringWithMask.removeRange(start - 1, start))
                            etCpf.setSelection(start - 1)
                        } else {

                            val str = replaceChars(charSequence.toString())
                            var cpfWithMask = ""

                            if (count == 0) { //is deleting
                                oldString
                                isUpdating = true
                            }

                            if (isUpdating) {
                                oldString = str
                                oldStringWithMask = charSequence.toString()
                                isUpdating = false
                                return
                            }

                            var i = 0
                            for (m: Char in mask.toCharArray()) {
                                if (m != '#' && (str.length > oldString.length || isDeletingDot)) {
                                    cpfWithMask += m
                                    continue
                                }
                                try {
                                    cpfWithMask += str.get(i)
                                } catch (e: Exception) {
                                    break
                                }
                                i++
                            }

                            oldStringWithMask = cpfWithMask

                            isUpdating = true
                            etCpf.setText(cpfWithMask)
                            if (cpfWithMask[cpfWithMask.length - 1] == '.'
                                || cpfWithMask[cpfWithMask.length - 1] == '-'
                                || cpfWithMask[cpfWithMask.length - 1] == '/'
                            )
                                etCpf.setSelection(start + 2)
                            else if (cpfWithMask.isNotEmpty() && cpfWithMask.length > (start + 1)) {
                                if (cpfWithMask[start] == '.'
                                    || cpfWithMask[start] == '/'
                                    || cpfWithMask[start] == '-'
                                )
                                    etCpf.setSelection(start + 2)
                                else if (cpfWithMask[start + 1] == '.'
                                    || cpfWithMask[start + 1] == '/'
                                    || cpfWithMask[start + 1] == '-'
                                )
                                    etCpf.setSelection(start + 2)
                                else
                                    etCpf.setSelection(start + 1)
                            } else
                                etCpf.setSelection(cpfWithMask.length)
                        }
                    }

                }

                override fun afterTextChanged(editable: Editable) {

                }
            }

            return textWatcher
        }

        private fun getDefaultMask(str: String): String {
            var defaultMask = MASK_CPF
            if (str.length > NUMBER_CHAR_DOCUMENT_CPF) {
                defaultMask = MASK_CNPJ
            }
            return defaultMask
        }
    }
}