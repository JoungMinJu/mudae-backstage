import React from 'react';
import { Container, Typography, Box, Button, Grid, Card, CardContent } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 8, textAlign: 'center' }}>
        <Typography variant="h2" component="h1" gutterBottom>
          Backstage Stories
        </Typography>
        <Typography variant="h5" color="text.secondary" paragraph>
          Share your thoughts and stories about performances
        </Typography>
        <Typography variant="body1" paragraph>
          From interpreting open endings to discussing memorable scenes, 
          engage in deep conversations with other audience members. 
          Enjoy performances on a whole new level.
        </Typography>
        
        <Box sx={{ mt: 4 }}>
          <Button 
            variant="contained" 
            size="large" 
            onClick={() => navigate('/performances')}
            sx={{ mr: 2 }}
          >
            View Performances
          </Button>
          <Button 
            variant="outlined" 
            size="large" 
            onClick={() => navigate('/posts')}
          >
            Join Discussions
          </Button>
        </Box>
      </Box>

      <Grid container spacing={4}>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
                              <Typography variant="h5" component="h2" gutterBottom>
                  🎭 Performance Info
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Check information about various performances, 
                  and share reviews and discussions.
                </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
                              <Typography variant="h5" component="h2" gutterBottom>
                  💬 Discussions & Comments
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Share your interpretations of open endings, 
                  and opinions about memorable scenes.
                </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
                              <Typography variant="h5" component="h2" gutterBottom>
                  👍 Voting & Reactions
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Agree or disagree with opinions, 
                  and explore diverse perspectives.
                </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Home;
