/*
 * Copyright (c) 2016-2017, Tinhvan Outsourcing JSC. All rights reserved.
 *
 * No permission to use, copy, modify and distribute this software
 * and its documentation for any purpose is granted.
 * This software is provided under applicable license agreement only.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.siblinks.ws.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.common.ErrorLevel;
import com.siblinks.ws.dao.CacheObjectDao;
import com.siblinks.ws.util.SibConstants;

/**
 * @author hungpd
 * @version 1.0
 */

@Repository
public class CacheObjectDaoImpl implements CacheObjectDao {
    private static final Logger logger = LoggerFactory.getLogger(CacheObjectDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    @Override
    @Cacheable(cacheNames = "wordFilter")
    public List getAllWordFilter() throws DAOException {
        List<Map<String, Object>> listWordFilter = null;
        try {
            String query = env.getProperty(SibConstants.SqlMapper.SQL_GET_ALL_WORD_FILTER);
            logger.debug("GET_ALL_WORD_FILTER");
            listWordFilter = jdbcTemplate.queryForList(query);
        } catch (NullPointerException | DataAccessException e) {
            throw new DAOException(e.getCause(), e.getMessage(), null, ErrorLevel.ERROR);
        }
        return listWordFilter;
    }

}
