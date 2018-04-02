package br.grupointegrado.tads.clima.util

import android.content.Context
import android.text.format.DateUtils
import br.grupointegrado.tads.clima.R
import java.text.SimpleDateFormat
import java.util.*

class DataUtils {

    companion object {

        val SEGUNDO_EM_MILISSEGUNDOS: Long = 1000
        val MINUTO_EM_MILISSEGUNDOS = SEGUNDO_EM_MILISSEGUNDOS * 60
        val HORA_EM_MILISSEGUNDOS = MINUTO_EM_MILISSEGUNDOS * 60
        val DIA_EM_MILISSEGUNDOS = HORA_EM_MILISSEGUNDOS * 24

        /**
         * Todas as tadas armazenadas estão no formato UTC, por isso é necessário converter
         * para o timezone do usuário antes de apresentar.
         *
         */
        fun convertDataUtcParaLocal(dataUtc: Long): Long {
            val tz = TimeZone.getDefault()
            val gmtOffset = tz.getOffset(dataUtc).toLong()
            return dataUtc - gmtOffset
        }

        /**
         * Antes de armazenar os dados é preciso converter as data para o timezone UTC.
         *
         */
        fun convertDataLocalParaUtc(dataLocal: Long): Long {
            val tz = TimeZone.getDefault()
            val gmtOffset = tz.getOffset(dataLocal).toLong()
            return dataLocal + gmtOffset
        }

        /**
         * Método que converte as datas em formatos amigáveis para ser apresentado.
         *
         */
        fun getDataAmigavelEmString(context: Context, dataEmMilissegundos: Long, exibirDataCompleta: Boolean): String {

            val dataLocal = convertDataUtcParaLocal(dataEmMilissegundos)
            val numeroDia = getNumeroDoDia(dataLocal)
            val numeroDiaAtual = getNumeroDoDia(System.currentTimeMillis())

            if (numeroDia == numeroDiaAtual || exibirDataCompleta) {

                val nomeDia = getNomeDoDia(context, dataLocal)
                val dataLegivel = getDataLegivel(context, dataLocal)
                if (numeroDia - numeroDiaAtual < 2) {
                    val nomeDiaLocal = SimpleDateFormat("EEEE").format(dataLocal)
                    return dataLegivel.replace(nomeDiaLocal, nomeDia)
                } else {
                    return dataLegivel
                }
            } else if (numeroDia < numeroDiaAtual + 7) {
                return getNomeDoDia(context, dataLocal)
            } else {
                val flags = (DateUtils.FORMAT_SHOW_DATE
                        or DateUtils.FORMAT_NO_YEAR
                        or DateUtils.FORMAT_ABBREV_ALL
                        or DateUtils.FORMAT_SHOW_WEEKDAY)

                return DateUtils.formatDateTime(context, dataLocal, flags)
            }
        }

        /**
         * Retorna a data em formato legível.
         *
         */
        private fun getDataLegivel(context: Context, timeInMillis: Long): String {
            val flags = (DateUtils.FORMAT_SHOW_DATE
                    or DateUtils.FORMAT_NO_YEAR
                    or DateUtils.FORMAT_SHOW_WEEKDAY)

            return DateUtils.formatDateTime(context, timeInMillis, flags)
        }

        /**
         * Retorna o nome do dia: "hoje", "amanhã", "segunda", etc.
         *
         */
        fun getNomeDoDia(context: Context, dateInMillis: Long): String {
            val dayNumber = getNumeroDoDia(dateInMillis)
            val currentDayNumber = getNumeroDoDia(System.currentTimeMillis())
            if (dayNumber == currentDayNumber) {
                return context.getString(R.string.hoje)
            } else if (dayNumber == currentDayNumber + 1) {
                return context.getString(R.string.amanha)
            } else {
                val dayFormat = SimpleDateFormat("EEEE")
                return dayFormat.format(dateInMillis)
            }
        }

        /**
         * Para facilitar a busca por uma data exata, nós normalizamos todas as datas que
         * serão armazenadas para o início do dia em UTC.
         *
         */
        fun normalizarData(date: Long): Long {
            return date / DIA_EM_MILISSEGUNDOS * DIA_EM_MILISSEGUNDOS
        }

        /**
         * Retorna o número do dia desde January 01, 1970, 12:00 Midnight UTC)
         *
         */
        fun getNumeroDoDia(date: Long): Long {
            val tz = TimeZone.getDefault()
            val gmtOffset = tz.getOffset(date).toLong()
            return (date + gmtOffset) / DIA_EM_MILISSEGUNDOS
        }

    }

}